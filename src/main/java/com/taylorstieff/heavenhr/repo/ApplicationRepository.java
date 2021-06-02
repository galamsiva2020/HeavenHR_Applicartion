package com.taylorstieff.heavenhr.repo;

import com.taylorstieff.heavenhr.model.Application;
import com.taylorstieff.heavenhr.model.Offer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * The ApplicationRepository handles the CRUD operations for {@link com.taylorstieff.heavenhr.model.Application}
 * objects
 */
@Repository
public interface ApplicationRepository extends PagingAndSortingRepository<Application, Long> {
    Page<Application> findApplicationsByRelatedOffer(Offer offer, Pageable pageRequest);
}
