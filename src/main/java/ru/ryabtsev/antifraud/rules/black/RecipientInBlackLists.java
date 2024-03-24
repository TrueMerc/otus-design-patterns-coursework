package ru.ryabtsev.antifraud.rules.black;

import java.util.List;
import ru.ryabtsev.antifraud.caches.BlackTinCache;
import ru.ryabtsev.antifraud.conditional.actions.ConditionalActionWithAlternative;
import ru.ryabtsev.antifraud.conditional.actions.ConditionalActions;
import ru.ryabtsev.antifraud.conditional.actions.DefaultConditionalAction;
import ru.ryabtsev.antifraud.rules.RuleConfiguration;
import ru.ryabtsev.antifraud.rules.actions.BasicIncidentGeneration;
import ru.ryabtsev.antifraud.rules.actions.RuleIsNotAppliedGeneration;
import ru.ryabtsev.antifraud.rules.actions.UnsupportedTransactionTypeGeneration;
import ru.ryabtsev.antifraud.rules.conditions.InstanceOfClass;
import ru.ryabtsev.antifraud.rules.conditions.PresenceInContainer;
import ru.ryabtsev.antifraud.transactions.ProcessableTransaction;
import ru.ryabtsev.antifraud.transactions.Transaction;
import ru.ryabtsev.antifraud.transactions.traits.PayeeTinContainer;

/**
 * The rule that checks if the recipient requisites are present in one of the black lists.
 */
public class RecipientInBlackLists implements BlackRule {

    private static final String INCIDENT_MESSAGE = "ИНН содержится в чёрном списке";

    private static final String DEFAULT_MESSAGE = "Нет совпадений по параметрам со списками запрещенных";

    private final BlackTinCache blackTinCache;

    private final RuleConfiguration ruleConfiguration;


    public RecipientInBlackLists(final RuleConfiguration ruleConfiguration, final BlackTinCache blackTinCache) {
        this.blackTinCache = blackTinCache;
        this.ruleConfiguration = ruleConfiguration;
    }

    @Override
    public ProcessableTransaction applyTo(final Transaction transaction) {
        final var defaultAction = new UnsupportedTransactionTypeGeneration(this, ruleConfiguration);
        final var presenceInContainer = new PresenceInContainer<>(
                transaction, PayeeTinContainer::getPayeeTin, blackTinCache
        );
        final var parametersAreNotPresentInBlackList = new RuleIsNotAppliedGeneration(
                this, ruleConfiguration, DEFAULT_MESSAGE
        );
        final var conditionalIncidentGeneration = new ConditionalActionWithAlternative<>(
                presenceInContainer,
                new BasicIncidentGeneration(this, ruleConfiguration, INCIDENT_MESSAGE),
                parametersAreNotPresentInBlackList
        );
        final var instanceOfClass = new InstanceOfClass(transaction, PayeeTinContainer.class);
        final var conditionalActions = new ConditionalActions<>(
                new DefaultConditionalAction<>(instanceOfClass, conditionalIncidentGeneration),
                defaultAction
        );
        final var ruleExecutionResult = conditionalActions.execute();
        return ruleExecutionResult.isIncident()
                ? ProcessableTransaction.ofFinal(transaction, ruleExecutionResult)
                : ProcessableTransaction.ofPartial(transaction, ruleExecutionResult);
    }
}
