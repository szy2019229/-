
package gkd.simple.mall.entity;

import lombok.Data;

@Data
public class SimpleMallOrderAddress {
    private Long orderId;

    private String userName;

    private String userPhone;

    private String provinceName;

    private String cityName;

    private String regionName;

    private String detailAddress;
}