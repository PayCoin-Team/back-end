package com.example.test.domain.history.repository.querydsl;

import com.example.test.domain.admin.dto.response.ResponseHistoryCounts;
import com.example.test.domain.history.entity.History;
import com.example.test.domain.history.enums.Type;
import com.example.test.domain.userwallet.entity.QUserWallet;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.test.domain.history.entity.QHistory.history;

public class HistoryRepositoryImpl implements HistoryRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public HistoryRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<History> searchHistories(Long userId, Integer year, Integer month, Type type, Pageable pageable) {

        QUserWallet senderWallet = new QUserWallet("senderWallet");
        QUserWallet receiverWallet = new QUserWallet("receiverWallet");

        // 쿼리 조회문
        List<History> content = queryFactory
                .selectFrom(history)
                .leftJoin(history.sender, senderWallet).fetchJoin()
                .leftJoin(senderWallet.user).fetchJoin()
                .leftJoin(history.receiver, receiverWallet).fetchJoin()
                .leftJoin(receiverWallet.user).fetchJoin()
                .where(
                        userEq(userId, senderWallet, receiverWallet),
                        dateBetween(year, month),
                        typeEq(userId, type, senderWallet, receiverWallet)
                )
                .orderBy(history.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 조회 된 로우 수
        JPAQuery<Long> countQuery = queryFactory
                .select(history.count())
                .from(history)
                .leftJoin(history.sender, senderWallet)
                .leftJoin(senderWallet.user)
                .leftJoin(history.receiver, receiverWallet)
                .leftJoin(receiverWallet.user)
                .where(
                        userEq(userId, senderWallet, receiverWallet),
                        dateBetween(year, month),
                        typeEq(userId, type, senderWallet, receiverWallet)
                );

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private BooleanExpression userEq(Long userId, QUserWallet senderWallet, QUserWallet receiverWallet) {
        if (userId == null) return null;
        return senderWallet.user.id.eq(userId)
                .or(receiverWallet.user.id.eq(userId));
    }

    private BooleanExpression dateBetween(Integer year, Integer month) {
        if (year == null || month == null) return null;
        LocalDateTime start = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime end = start.plusMonths(1);
        return history.createdAt.goe(start).and(history.createdAt.lt(end));
    }

    private BooleanExpression typeEq(Long userId, Type type, QUserWallet senderWallet, QUserWallet receiverWallet) {
        if (type == null) return null;
        if (Type.DEPOSIT.equals(type)) return receiverWallet.user.id.eq(userId);
        if (Type.WITHDRAWAL.equals(type)) return senderWallet.user.id.eq(userId);
        return null;
    }

    // 오늘 송금한 회원 조회
    @Override
    public List<Long> findActiveSenderIds(LocalDateTime start, LocalDateTime end) {
        QUserWallet senderWallet = new QUserWallet("senderWallet");
        return queryFactory
                .select(senderWallet.user.id)
                .from(history)
                .join(history.sender, senderWallet)
                .where(history.createdAt.goe(start).and(history.createdAt.lt(end)))
                .distinct()
                .fetch();
    }

    @Override
    public List<Long> findActiveReceiverIds(LocalDateTime start, LocalDateTime end) {
        QUserWallet receiverWallet = new QUserWallet("receiverWallet");
        return queryFactory
                .select(receiverWallet.user.id)
                .from(history)
                .join(history.receiver, receiverWallet)
                .where(history.createdAt.goe(start).and(history.createdAt.lt(end)))
                .distinct()
                .fetch();
    }

    // 오늘 거래한 횟수 및 금액
    @Override
    public ResponseHistoryCounts historyTodayCounts(LocalDateTime start, LocalDateTime end) {
        return queryFactory
                .select(Projections.constructor(ResponseHistoryCounts.class,
                                history.count(),
                                history.amount.sum()
                        )
                )
                .from(history)
                .where(history.createdAt.goe(start).and(history.createdAt.lt(end)))
                .fetchOne();
    }
}