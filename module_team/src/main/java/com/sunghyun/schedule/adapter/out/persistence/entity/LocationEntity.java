package com.sunghyun.schedule.adapter.out.persistence.entity;

import com.sunghyun.schedule.domain.Location;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "location")
@AllArgsConstructor
@NoArgsConstructor
public class LocationEntity {

    @Id
    @Column
    private Long locationNo;

    @Column
    private String locationName;

    @Column
    private String address;

    @Column
    private String roadAddress;

    @Column
    private String addressX;

    @Column
    private String addressY;

    public static LocationEntity from(Location location) {
        return new LocationEntity(
                location.getLocationNo(),
                location.getLocationName(),
                location.getAddress(),
                location.getRoadAddress(),
                location.getAddressX(),
                location.getAddressY()
        );
    }

    public Location toDomain() {
        return Location.builder()
                .locationNo(this.locationNo)
                .locationName(this.locationName)
                .address(this.address)
                .roadAddress(this.roadAddress)
                .addressX(this.addressX)
                .addressY(this.addressY)
                .build()
                ;
    }
}
