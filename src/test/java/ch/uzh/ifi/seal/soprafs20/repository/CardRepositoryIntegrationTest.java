package ch.uzh.ifi.seal.soprafs20.repository;

import ch.uzh.ifi.seal.soprafs20.constant.CardType;
import ch.uzh.ifi.seal.soprafs20.constant.DevelopmentType;
import ch.uzh.ifi.seal.soprafs20.constant.ResourceType;
import ch.uzh.ifi.seal.soprafs20.entity.gameEntities.cards.Card;
import ch.uzh.ifi.seal.soprafs20.entity.gameEntities.cards.DevelopmentCard;
import ch.uzh.ifi.seal.soprafs20.entity.gameEntities.cards.ResourceCard;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class CardRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Qualifier("cardRepository")
    @Autowired
    private CardRepository cardRepository;

    @Test
    public void testGetCardsByType_success() {

        //given
        DevelopmentCard dCard = new DevelopmentCard();
        dCard.setDevelopmentType(DevelopmentType.KNIGHT);
        ResourceCard rCard = new ResourceCard();
        rCard.setResourceType(ResourceType.BRICK);

        entityManager.persist(dCard);
        entityManager.persist(rCard);
        entityManager.flush();

        List<Card> dResult = cardRepository.findAllByType(CardType.DEVELOPMENT);
        List<Card> rResult = cardRepository.findAllByType(CardType.RESOURCE);

        assertEquals(1, dResult.size(), "The array should contain 1 element!");
        assertEquals(1, rResult.size(), "The array should contain 1 element!");
        assertEquals(DevelopmentCard.class, dResult.get(0).getClass(),
                "The result should be of class DevelopmentCard!");
        assertEquals(ResourceCard.class, rResult.get(0).getClass(),
                "The result should be of class ResourceCard!");
    }
}
