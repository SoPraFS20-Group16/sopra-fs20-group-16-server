package ch.uzh.ifi.seal.soprafs20.repository;

import ch.uzh.ifi.seal.soprafs20.entity.game.ResourceWallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("resourceWalletRepository")
public interface ResourceWalletRepository extends JpaRepository<ResourceWallet, Long> {
}
