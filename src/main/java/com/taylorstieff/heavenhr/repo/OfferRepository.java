package com.taylorstieff.heavenhr.repo;

import com.taylorstieff.heavenhr.model.Offer;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * The OfferRepository handles the CRUD operations for {@link com.taylorstieff.heavenhr.model.Offer} objects
 */
@Repository
public interface OfferRepository extends PagingAndSortingRepository<Offer, Long> {
}
