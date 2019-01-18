package demo.springboot.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_park_conf")
public class ParkConf  {
    @Id
    private Long id;

    @Column(name = "store_rate")
    private int storeRate;

    public int getStoreRate() {
        return storeRate;
    }

    public void setStoreRate(int storeRate) {
        this.storeRate = storeRate;
    }
}

