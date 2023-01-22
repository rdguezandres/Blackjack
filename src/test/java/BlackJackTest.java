import org.junit.Test;

import java.util.stream.Stream;

import static org.junit.Assert.*;

public class BlackJackTest {
    @Test
    public void given_one_card_should_return_value() {
        assertEquals(10, createHand(Card._10).value());
        assertEquals(5, createHand(Card._5).value());
        assertEquals(10, createHand(Card.Jack).value());
        assertEquals(10, createHand(Card.Queen).value());
        assertEquals(10, createHand(Card.King).value());
    }

    @Test
    public void given_two_cards_should_return_value() {
        assertEquals(20, createHand(Card._10, Card._10).value());
        assertEquals(15, createHand(Card._10, Card._5).value());
        assertEquals(20, createHand(Card.Jack, Card._10).value());
        assertEquals(20, createHand(Card.Queen, Card._10).value());
        assertEquals(20, createHand(Card.King, Card._10).value());
        assertEquals(12, createHand(Card.Ace, Card.Ace).value());
    }

    @Test
    public void given_two_cards_should_determine_if_blackjack() {
        assertTrue(createHand(Card._10, Card.Ace).isBlackJack());
        assertTrue(createHand(Card.Ace, Card._10).isBlackJack());
        assertFalse(createHand(Card._10, Card._10).isBlackJack());
        assertFalse(createHand(Card._10, Card._5).isBlackJack());
        assertFalse(createHand(Card.Jack, Card._10).isBlackJack());
        assertFalse(createHand(Card.Queen, Card._10).isBlackJack());
        assertFalse(createHand(Card.King, Card._10).isBlackJack());
        assertFalse(createHand(Card.Ace, Card.Ace).isBlackJack());
    }

    @Test
    public void given_three_cards_should_determine_if_blackjack() {
        assertFalse(createHand(Card._9, Card.Ace, Card.Ace).isBlackJack());
        assertFalse(createHand(Card._10, Card._10, Card.Ace).isBlackJack());
        assertFalse(createHand(Card._10, Card._5, Card.Ace).isBlackJack());
    }

    @Test
    public void given_two_cards_should_determine_if_bust() {
        assertFalse(createHand(Card._10, Card._10).isBust());
        assertFalse(createHand(Card._10, Card._5).isBust());
        assertFalse(createHand(Card.Jack, Card._10).isBust());
        assertFalse(createHand(Card.Queen, Card._10).isBust());
    }

    @Test
    public void given_three_cards_should_determine_if_bust() {
        assertFalse(createHand(Card._10, Card._10, Card.Ace).isBust());
        assertFalse(createHand(Card._10, Card._5, Card.Ace).isBust());
        assertFalse(createHand(Card.Jack, Card._10, Card.Ace).isBust());
        assertFalse(createHand(Card.Queen, Card._10, Card.Ace).isBust());
        assertTrue(createHand(Card._10, Card._10, Card._10).isBust());
    }

    private Hand createHand(Card... cards) {
        return new Hand() {
            @Override
            public int value() {
                return canUseAceExtendedValue() ? sum() + 10 : sum();
            }

            @Override
            public boolean isBust() {
                return value() > 21;
            }

            @Override
            public boolean isBlackJack() {
                return value() == 21 && cards.length == 2;
            }

            private boolean canUseAceExtendedValue() {
                return sum() <= 11 && containsAce();
            }

            private boolean containsAce() {
                return Stream.of(cards).anyMatch(card -> card == Card.Ace);
            }

            private int sum() {
                return Stream.of(cards).mapToInt(Card::value).sum();
            }
        };
    }

    public interface Hand {
        int value();
        boolean isBust();
        boolean isBlackJack();
    }

    public enum Card {
        Ace, _2, _3, _4, _5, _6, _7, _8, _9, _10, Jack, Queen, King;

        public boolean isFace() {
            return this == Jack || this == Queen || this == King;
        }

        public int value() {
            return isFace() ? 10 : ordinal() + 1;
        }
    }

}
