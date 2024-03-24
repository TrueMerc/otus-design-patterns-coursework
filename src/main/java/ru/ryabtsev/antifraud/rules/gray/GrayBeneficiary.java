package ru.ryabtsev.antifraud.rules.gray;

import ru.ryabtsev.antifraud.caches.GrayCardNumberCache;
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
import ru.ryabtsev.antifraud.transactions.traits.DestinationCardNumberContainer;

public class GrayBeneficiary implements GrayRule {

    private static final String SUCCESSFUL_APPLICATION_MESSAGE = "Получатель находится в сером списке по номеру карты";

    private static final String UNSUCCESSFUL_APPLICATION_MESSAGE =
            "Нет совпадений по параметрам со списками подозрительных";

    private final RuleConfiguration ruleConfiguration;

    private final GrayCardNumberCache grayCardNumberCache;

    public GrayBeneficiary(final RuleConfiguration ruleConfiguration, final GrayCardNumberCache grayCardNumberCache) {
        this.ruleConfiguration = ruleConfiguration;
        this.grayCardNumberCache = grayCardNumberCache;
    }

    @Override
    public ProcessableTransaction applyTo(final Transaction transaction) {
        final var defaultAction = new UnsupportedTransactionTypeGeneration(this, ruleConfiguration);
        final var presenceInContainer = new PresenceInContainer<>(
                transaction, DestinationCardNumberContainer::getDestinationCardNumber, grayCardNumberCache
        );
        final var cardNumberIsNotPresentInGrayList = new RuleIsNotAppliedGeneration(
                this, ruleConfiguration, UNSUCCESSFUL_APPLICATION_MESSAGE
        );
        final var conditionalIncidentGeneration = new ConditionalActionWithAlternative<>(
                presenceInContainer,
                new BasicIncidentGeneration(this, ruleConfiguration, SUCCESSFUL_APPLICATION_MESSAGE),
                cardNumberIsNotPresentInGrayList
        );
        final var instanceOfClass = new InstanceOfClass(transaction, DestinationCardNumberContainer.class);
        final var conditionalActions = new ConditionalActions<>(
                new DefaultConditionalAction<>(instanceOfClass, conditionalIncidentGeneration),
                defaultAction
        );
        return ProcessableTransaction.ofPartial(transaction, conditionalActions.execute());
    }
}
