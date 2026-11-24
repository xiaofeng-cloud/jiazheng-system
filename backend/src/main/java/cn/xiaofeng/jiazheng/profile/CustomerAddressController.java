package cn.xiaofeng.jiazheng.profile;

import cn.xiaofeng.jiazheng.auth.LoginUser;
import cn.xiaofeng.jiazheng.common.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/customer/addresses")
@PreAuthorize("hasRole('CUSTOMER')")
public class CustomerAddressController {
    private final CustomerAddressMapper mapper;
    public CustomerAddressController(CustomerAddressMapper mapper) { this.mapper = mapper; }
    @GetMapping
    public ApiResponse<List<CustomerAddressMapper.AddressRow>> list(org.springframework.security.core.Authentication authentication) { return ApiResponse.success(mapper.findByCustomer(((LoginUser) authentication.getPrincipal()).userId())); }
    @PostMapping
    public ApiResponse<Long> create(@Valid @RequestBody AddressRequest request, org.springframework.security.core.Authentication authentication) {
        long customerId = ((LoginUser) authentication.getPrincipal()).userId(); long id = mapper.nextId();
        mapper.insert(new CustomerAddressMapper.AddressRow(id, customerId, request.receiverName(), request.receiverPhone(), request.province(), request.city(), request.district(), request.detailAddress(), 0));
        return ApiResponse.success("地址已保存", id);
    }
    public record AddressRequest(@NotBlank String receiverName, @NotBlank String receiverPhone, @NotBlank String province, @NotBlank String city, @NotBlank String district, @NotBlank String detailAddress) {}
}
