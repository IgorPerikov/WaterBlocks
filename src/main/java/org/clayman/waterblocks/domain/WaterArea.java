package org.clayman.waterblocks.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table
public class WaterArea extends DBSuperClass {
    @ManyToOne
    @JoinColumn(name = "result_id")
    @JsonBackReference(value = "result-waterarea")
    private Result result;

    private int leftBorder;
    private int rightBorder;

    public int getLeftBorder() {
        return leftBorder;
    }

    public void setLeftBorder(int leftBorder) {
        this.leftBorder = leftBorder;
    }

    public int getRightBorder() {
        return rightBorder;
    }

    public void setRightBorder(int rightBorder) {
        this.rightBorder = rightBorder;
    }

    public WaterArea() {

    }

    public WaterArea(int leftBorder, int rightBorder) {
        this.leftBorder = leftBorder;
        this.rightBorder = rightBorder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WaterArea waterArea = (WaterArea) o;

        if (leftBorder != waterArea.leftBorder) return false;
        return rightBorder == waterArea.rightBorder;
    }

    @Override
    public int hashCode() {
        int result = leftBorder;
        result = 31 * result + rightBorder;
        return result;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }
}
