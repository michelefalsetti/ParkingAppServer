package com.scrum.parkingapp.dto;

import lombok.Data;

@Data
public class AddressDto {

    private Long id;

    private String street;

    private String city;

    private Double latitude;

    private Double longitude;

}
