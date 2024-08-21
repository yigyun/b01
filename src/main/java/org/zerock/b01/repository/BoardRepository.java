package org.zerock.b01.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.zerock.b01.domain.Board;
import org.zerock.b01.repository.search.BoardSearch;

import java.util.Optional;

/**
 *  기본적인 메소드는 연계방식만 알면 쿼리 메소드로 대부분 지원한다.
 *  그런데 동적쿼리나, 다루기 어려운 부분은 추가로 QueryDsl로 확장하자.
 *  실무에서는 쿼리 메소드는 진짜 단순한 곳에서 활용되고, JPQL, @Query로 처리하는게 더 가독성이 뛰어나고 조인과 같은 복잡한 쿼리, 원하는 속성 처리 및 DTO로 처리, 특정 데이터베이스에 적용되는 nativeQuery 가능
 *
 */

public interface BoardRepository extends JpaRepository<Board, Long>, BoardSearch {

    // nativeQuery 예시
    @Query(value = "select now()", nativeQuery = true)
    String getTime();


    /**
     * 지연 로딩에서 한 번에 조인해서 select하는 방식이다.
     * attributePaths 속성으로 로딩해야 하는 속성을 명시한다.
     * @param bno
     * @return
     */
    @EntityGraph(attributePaths = {"imageSet"})
    @Query("select b from Board b where b.bno = :bno")
    Optional<Board> findByIdWithImages(Long bno);

}
