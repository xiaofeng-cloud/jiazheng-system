package cn.xiaofeng.jiazheng.demand;

import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface DemandMapper {
    @Select("SELECT id, demand_no AS demandNo, customer_id AS customerId, service_item_id AS serviceItemId, address_id AS addressId, expected_date AS expectedDate, expected_time AS expectedTime, description, status FROM service_demand WHERE customer_id = #{customerId} ORDER BY id DESC")
    List<DemandRow> findByCustomer(Long customerId);
    @Select("SELECT id, demand_no AS demandNo, customer_id AS customerId, service_item_id AS serviceItemId, address_id AS addressId, expected_date AS expectedDate, expected_time AS expectedTime, description, status FROM service_demand ORDER BY id DESC")
    List<DemandRow> findAll();
    @Select("SELECT id FROM customer_address WHERE id = #{addressId} AND customer_id = #{customerId}")
    Long findOwnedAddress(Long customerId, Long addressId);
    @Insert("INSERT INTO service_demand(id, demand_no, customer_id, service_item_id, address_id, expected_date, expected_time, description, status) VALUES (#{id}, #{demandNo}, #{customerId}, #{serviceItemId}, #{addressId}, #{expectedDate}, #{expectedTime}, #{description}, '待审核')")
    int insert(DemandRow row);
    @Update("UPDATE service_demand SET status = #{status} WHERE id = #{id}")
    int updateStatus(Long id, String status);
    @Select("SELECT COALESCE(MAX(id),0)+1 FROM service_demand")
    Long nextId();
    record DemandRow(Long id, String demandNo, Long customerId, Long serviceItemId, Long addressId, java.time.LocalDate expectedDate, String expectedTime, String description, String status) {}
}
