package ru.ryabtsev.antifraud.rules.black;

import java.util.List;
import ru.ryabtsev.antifraud.caches.BlackTinCache;
import ru.ryabtsev.antifraud.conditional.actions.ConditionalActionWithAlternative;
import ru.ryabtsev.antifraud.conditional.actions.ConditionalActions;
import ru.ryabtsev.antifraud.conditional.actions.DefaultConditionalAction;
import ru.ryabtsev.antifraud.rules.RuleConfiguration;
import ru.ryabtsev.antifraud.rules.actions.BasicIncidentGeneration;
import ru.ryabtsev.antifraud.rules.actions.BasicRuleResultGeneration;
import ru.ryabtsev.antifraud.rules.condiitons.InstanceOfClass;
import ru.ryabtsev.antifraud.rules.condiitons.PresenceInContainer;
import ru.ryabtsev.antifraud.rules.results.RuleIsNotApplied;
import ru.ryabtsev.antifraud.transactions.ProcessableTransaction;
import ru.ryabtsev.antifraud.transactions.Transaction;
import ru.ryabtsev.antifraud.transactions.traits.PayeeTinContainer;

/**
 * The rule that checks if the recipient requisites are present in one of the black lists.
 */
public class RecipientInBlackLists implements BlackRule {

    private static final String INCIDENT_MESSAGE = "ИНН содержится в чёрном списке";


    private final BlackTinCache blackTinCache;

    private final RuleConfiguration ruleConfiguration;


    public RecipientInBlackLists(final RuleConfiguration ruleConfiguration, final BlackTinCache blackTinCache) {
        this.blackTinCache = blackTinCache;
        this.ruleConfiguration = ruleConfiguration;

    }

    @Override
    public ProcessableTransaction applyTo(final Transaction transaction) {
        final var defaultAction = new BasicRuleResultGeneration(new RuleIsNotApplied(this, ruleConfiguration));
        final var presenceInContainer = new PresenceInContainer<>(
                ((PayeeTinContainer) transaction).getPayeeTin(), blackTinCache
        );
        final var conditionalIncidentGeneration = new ConditionalActionWithAlternative<>(
                presenceInContainer,
                new BasicIncidentGeneration(this, ruleConfiguration, INCIDENT_MESSAGE),
                defaultAction
        );
        final var instanceOfClass = new InstanceOfClass(transaction, PayeeTinContainer.class);
        final var conditionalActions = new ConditionalActions<>(
                List.of(
                        new DefaultConditionalAction<>(instanceOfClass, conditionalIncidentGeneration)
                ),
                defaultAction
        );
        final var ruleExecutionResult = conditionalActions.execute();
        return ruleExecutionResult.isIncident()
                ? ProcessableTransaction.ofFinal(transaction, ruleExecutionResult)
                : ProcessableTransaction.ofPartial(transaction, ruleExecutionResult);
    }
}
