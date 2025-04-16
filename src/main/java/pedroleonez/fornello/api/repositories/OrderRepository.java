package pedroleonez.fornello.api.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pedroleonez.fornello.api.entities.Order;
import pedroleonez.fornello.api.enums.Status;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("select o from Order o where o.id = :orderId and o.user.id = :userId")
    Optional<Order> findByOrderIdAndUserId(@Param("orderId") Long orderId, @Param("userId") Long userId);

    @Query("select o from Order o where o.user.id = :userId")
    Page<Order> findAllByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("select o from Order o where o.status = :status and o.user.id = :userId")
    Page<Order> findOrderByStatusAndUser(@Param("status") Status status, @Param("userId") Long userId, Pageable pageable);

    Page<Order> findByStatus(Status statusName, Pageable pageable);

    Page<Order> findAll(Pageable pageable);

    @Query(nativeQuery = true, value = "select o.* from orders o join users u on o.user_id = u.id where u.id = :userId limit 1")
    Optional<Order> findFirstByUserId(@Param("userId") Long userId);

}
