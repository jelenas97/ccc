package com.advertisement.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Advertisement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Integer kilometresLimit;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "car_id")
    public Car car;
    @Column
    public Long ownerId;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    public PriceList priceList;
    @Column
    private Integer discount;
    @Column
    private String place;
    @Column
    private Boolean cdw;
    @Column(name = "startDate", nullable = false)
    private LocalDate startDate;
    @Column(name = "endDate", nullable = false)
    private LocalDate endDate;

    @OneToMany(mappedBy = "advertisement", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Term> terms;

    @Override
    public String toString() {
        return "Advertisement{" +
                "id=" + id +
                ", kilometresLimit=" + kilometresLimit +
                ", car=" + car +
                ", ownerId=" + ownerId +
                ", priceList=" + priceList +
                ", discount=" + discount +
                ", place='" + place + '\'' +
                ", cdw=" + cdw +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
