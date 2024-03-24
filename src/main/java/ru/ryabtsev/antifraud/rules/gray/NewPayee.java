package ru.ryabtsev.antifraud.rules.gray;

import java.time.Duration;
import java.util.List;
import ru.ryabtsev.antifraud.caches.QuarantinedCardNumberCache;
import ru.ryabtsev.antifraud.caches.TrustedCardNumberCache;
import ru.ryabtsev.antifraud.conditional.actions.ConditionalActionWithAlternative;
import ru.ryabtsev.antifraud.conditional.actions.ConditionalActions;
import ru.ryabtsev.antifraud.conditional.actions.DefaultConditionalAction;
import ru.ryabtsev.antifraud.rules.RuleConfiguration;
import ru.ryabtsev.antifraud.rules.actions.BasicIncidentGeneration;
import ru.ryabtsev.antifraud.rules.actions.RuleIsNotAppliedGeneration;
import ru.ryabtsev.antifraud.rules.actions.UnsupportedTransactionTypeGeneration;
import ru.ryabtsev.antifraud.rules.conditions.CardNumberIsNotPresentInQuarantine;
import ru.ryabtsev.antifraud.rules.conditions.CardNumberPresentsInQuarantineForLongTime;
import ru.ryabtsev.antifraud.rules.conditions.InstanceOfClass;
import ru.ryabtsev.antifraud.rules.conditions.PresenceInContainer;
import ru.ryabtsev.antifraud.rules.conditions.PropertyAccessor;
import ru.ryabtsev.antifraud.rules.results.RuleIsApplied;
import ru.ryabtsev.antifraud.transactions.ProcessableTransaction;
import ru.ryabtsev.antifraud.transactions.Transaction;
import ru.ryabtsev.antifraud.transactions.traits.DestinationCardNumberContainer;

public class NewPayee implements GrayRule {

    private static final String PAYEE_IN_WHITE_LIST = "Получатель найден в списке разрешенных";

    private static final String ALREADY_PRESENT_IN_QUARANTINE = "Получатель уже находится в карантине";

    private static final String RECENTLY_PRESENT_IN_QUARANTINE = "Получатель недавно находится в карантине";

    private static final String NEW_PAYEE = "Платеж в сторону нового получателя";

    private final RuleConfiguration ruleConfiguration;

    private final TrustedCardNumberCache trustedCardNumberCache;

    private final QuarantinedCardNumberCache quarantinedCardNumberCache;

    public NewPayee(RuleConfiguration ruleConfiguration, TrustedCardNumberCache trustedCardNumberCache,
                    QuarantinedCardNumberCache quarantinedCardNumberCache) {
        this.ruleConfiguration = ruleConfiguration;
        this.trustedCardNumberCache = trustedCardNumberCache;
        this.quarantinedCardNumberCache = quarantinedCardNumberCache;
    }

    @Override
    public ProcessableTransaction applyTo(final Transaction transaction) {
        final var defaultAction = new UnsupportedTransactionTypeGeneration(this, ruleConfiguration);
        final var presenceInTrustedCardNumbersCache = new PresenceInContainer<>(
                transaction, DestinationCardNumberContainer::getDestinationCardNumber, trustedCardNumberCache
        );
        final var cardNumberAccessor = new PropertyAccessor<>(
                transaction, DestinationCardNumberContainer::getDestinationCardNumber);
        final var cardNumberIsNotPresentInQuarantine = new CardNumberIsNotPresentInQuarantine(
                cardNumberAccessor,
                quarantinedCardNumberCache
        );
        final var cardNumberIsNewOrPresentInQuarantineRecently = new ConditionalActionWithAlternative<>(
                cardNumberIsNotPresentInQuarantine,
                new BasicIncidentGeneration(this, ruleConfiguration, NEW_PAYEE),
                new BasicIncidentGeneration(this, ruleConfiguration, RECENTLY_PRESENT_IN_QUARANTINE)
        );
        final var presentInQuarantineForLongTime = new CardNumberPresentsInQuarantineForLongTime(
                new PropertyAccessor<>(transaction, DestinationCardNumberContainer::getDestinationCardNumber),
                quarantinedCardNumberCache,
                Duration.ofHours(1)
        );
        final var cardNumberPresenceInQuarantineCheck = new ConditionalActionWithAlternative<>(
                presentInQuarantineForLongTime,
                new RuleIsNotAppliedGeneration(this, ruleConfiguration, ALREADY_PRESENT_IN_QUARANTINE),
                cardNumberIsNewOrPresentInQuarantineRecently
        );
        final var instanceOfClass = new InstanceOfClass(transaction, DestinationCardNumberContainer.class);
        final var cardNumberPresenceCheck = new ConditionalActionWithAlternative<>(
                presenceInTrustedCardNumbersCache,
                new RuleIsNotAppliedGeneration(this, ruleConfiguration, PAYEE_IN_WHITE_LIST),
                cardNumberPresenceInQuarantineCheck
        );
        final var conditionalActions = new ConditionalActions<>(
                List.of(new DefaultConditionalAction<>(instanceOfClass, cardNumberPresenceCheck)),
                defaultAction
        );
        final var ruleExecutionResult = conditionalActions.execute();
        return RuleIsApplied.STATUS.equals(ruleExecutionResult.getStatus())
                ? ProcessableTransaction.ofFinal(transaction, ruleExecutionResult)
                : ProcessableTransaction.ofPartial(transaction, ruleExecutionResult);
    }
}
