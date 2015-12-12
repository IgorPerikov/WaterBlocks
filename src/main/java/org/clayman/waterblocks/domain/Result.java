package org.clayman.waterblocks.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.*;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Set;

@Entity
@Table
public class Result extends DBSuperClass {
    @Column(nullable = false, length = 10000)
    private String sequence;

    @Column(nullable = false)
    private Timestamp added = new Timestamp(new Date().getTime());

    @OneToMany(mappedBy = "result", fetch = FetchType.EAGER)
    @Cascade(CascadeType.ALL)
    @JsonManagedReference(value = "result-waterarea")
    private Set<WaterArea> waterAreas;

    private int waterCapacity;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Result result = (Result) o;

        return sequence.equals(result.sequence);
    }

    @Override
    public int hashCode() {
        return sequence.hashCode();
    }

    public int getWaterCapacity() {
        return waterCapacity;
    }

    public void setWaterCapacity(int waterCapacity) {
        this.waterCapacity = waterCapacity;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public Timestamp getAdded() {
        return added;
    }

    public void setAdded(Timestamp added) {
        this.added = added;
    }

    public Set<WaterArea> getWaterAreas() {
        return waterAreas;
    }

    public void setWaterAreas(Set<WaterArea> waterAreas) {
        this.waterAreas = waterAreas;
    }
}
