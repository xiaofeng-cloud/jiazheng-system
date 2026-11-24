package cn.xiaofeng.jiazheng.profile;

import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface CustomerAddressMapper {
    @Select("SELECT id, receiver_name AS receiverName, receiver_phone AS receiverPhone, province, city, district, detail_address AS detailAddress, is_default AS isDefault FROM customer_address WHERE customer_id = #{customerId} ORDER BY is_default DESC, id DESC")
    List<AddressRow> findByCustomer(Long customerId);
    @Insert("INSERT INTO customer_address(id, customer_id, receiver_name, receiver_phone, province, city, district, detail_address, is_default) VALUES (#{id}, #{customerId}, #{receiverName}, #{receiverPhone}, #{province}, #{city}, #{district}, #{detailAddress}, 0)")
    int insert(AddressRow row);
    @Select("SELECT COALESCE(MAX(id),3000)+1 FROM customer_address")
    Long nextId();
    record AddressRow(Long id, Long customerId, String receiverName, String receiverPhone, String province, String city, String district, String detailAddress, Integer isDefault) {}
}
