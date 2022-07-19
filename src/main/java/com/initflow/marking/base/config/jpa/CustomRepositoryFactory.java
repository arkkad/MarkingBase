

package com.initflow.marking.base.config.jpa;

import com.initflow.marking.base.repository.QueryableReadRepository;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.QuerydslUtils;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryComposition;
import org.springframework.data.repository.core.support.RepositoryFragment;

import javax.persistence.EntityManager;
import java.io.Serializable;

public class CustomRepositoryFactory extends JpaRepositoryFactory {

    private final EntityManager entityManager;

    public CustomRepositoryFactory(EntityManager entityManager) {
        super(entityManager);
        this.entityManager = entityManager;
    }

    @Override
    protected RepositoryComposition.RepositoryFragments getRepositoryFragments(RepositoryMetadata metadata) {
        RepositoryComposition.RepositoryFragments fragments = super.getRepositoryFragments(metadata);

        if (QueryableReadRepository.class.isAssignableFrom(
                metadata.getRepositoryInterface())) {

            JpaEntityInformation<?, Serializable> entityInformation =
                    getEntityInformation(metadata.getDomainType());

            Object queryableFragment = getTargetRepositoryViaReflection(
                    QueryableReadRepositoryImpl.class, entityInformation, entityManager);

            fragments = RepositoryComposition.RepositoryFragments.empty();
            boolean isQueryDslRepository = QuerydslUtils.QUERY_DSL_PRESENT && QuerydslPredicateExecutor.class.isAssignableFrom(metadata.getRepositoryInterface());
            if (isQueryDslRepository) {
                if (metadata.isReactiveRepository()) {
                    throw new InvalidDataAccessApiUsageException("Cannot combine Querydsl and reactive repository support in a single interface");
                }

//                JpaEntityInformation<?, Serializable> entityInformation = this.getEntityInformation(metadata.getDomainType());

                fragments = fragments.append(RepositoryFragment.implemented(queryableFragment));
//                Object querydslFragment = this.getTargetRepositoryViaReflection(QuerydslJpaPredicateExecutor.class, new Object[]{entityInformation, this.entityManager, this.entityPathResolver, this.crudMethodMetadataPostProcessor.getCrudMethodMetadata()});
//                fragments = fragments.append(RepositoryFragment.implemented(querydslFragment));
            }

        }

        return fragments;
    }
}