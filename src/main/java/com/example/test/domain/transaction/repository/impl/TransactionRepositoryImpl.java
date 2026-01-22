package com.example.test.domain.transaction.repository.impl;

import com.example.test.domain.transaction.enums.Type;
import com.example.test.domain.transaction.entity.Transaction;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.test.domain.transaction.entity.QTransaction.transaction;
import static com.example.test.domain.externalWallet.entity.QExternalWallet.externalWallet;

public class TransactionRepositoryImpl implements  TransactionRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public TransactionRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<Transaction> searchTransaction(Long userId, Integer year, Integer month, Type type, Pageable pageable) {

        List<Transaction> content = queryFactory
                .selectFrom(transaction)
                .leftJoin(transaction.externalWallet, externalWallet)
                .fetchJoin()
                .where(
                        userEq(userId),
                        typeEq(type),
                        dateBetween(year, month)
                )
                .orderBy(transaction.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = queryFactory
                .select(transaction.count())
                .from(transaction)
                .leftJoin(transaction.externalWallet, externalWallet)
                .where(
                        userEq(userId),
                        dateBetween(year, month),
                        typeEq(type)
                )
                .fetchOne();



        return new PageImpl<>(content, pageable, count != null ? count : 0L);
    }

    private BooleanExpression userEq(Long userId) {
        return userId != null ? externalWallet.user.id.eq(userId) : null;
    }

    private BooleanExpression typeEq(Type type) {
        return type != null ? transaction.type.stringValue().eq(type.name()) : null;
    }

    private BooleanExpression dateBetween(Integer year, Integer month) {
        if (year == null || month == null) return null;
        LocalDateTime start = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime end = start.plusMonths(1);
        return transaction.createdAt.goe(start).and(transaction.createdAt.lt(end));
    }
}
