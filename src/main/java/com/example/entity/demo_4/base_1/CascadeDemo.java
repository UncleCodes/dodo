package com.example.entity.demo_4.base_1;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import org.hibernate.annotations.DynamicInsert;

import com.dodo.common.annotation.action.DodoAction;
import com.dodo.common.annotation.action.DodoEntity;
import com.dodo.common.annotation.cascade.DodoCascade;
import com.dodo.common.annotation.field.DodoField;
import com.dodo.common.annotation.field.DodoViewGroup;
import com.dodo.common.annotation.menu.DodoMenu;
import com.dodo.common.framework.entity.BaseEntity;
import com.dodo.privilege.entity.admin_1.location_6.City;
import com.dodo.privilege.entity.admin_1.location_6.Country;
import com.dodo.privilege.entity.admin_1.location_6.District;
import com.dodo.privilege.entity.admin_1.location_6.Province;

/**
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
@Entity
@DynamicInsert
@DodoEntity(
        name = "级联",
        actions = { DodoAction.ALL },
        levelOne = @DodoMenu(name = "Demo系统", sortSeq = 7),
        levelTwo = @DodoMenu(name = "基础演示", sortSeq = 1),
        levelThree = @DodoMenu(name = "级联演示", sortSeq = 8))
public class CascadeDemo extends BaseEntity {

    private static final long serialVersionUID = 2236186294198415373L;

    @DodoViewGroup(groupSeq = 1, groupName = "第一组级联")
    @DodoField(name = "国家", sortSeq = 2, infoTip = "切换时，省份级联")
    @DodoCascade(group = 0)
    private Country           country;

    @DodoViewGroup(groupSeq = 1, groupName = "第一组级联")
    @DodoField(name = "省份", sortSeq = 3, infoTip = "切换时，城市级联")
    @DodoCascade(group = 0, parentField = "country")
    private Province          province;

    @DodoViewGroup(groupSeq = 1, groupName = "第一组级联")
    @DodoField(name = "城市", sortSeq = 4, infoTip = "切换时，区域级联")
    @DodoCascade(group = 0, parentField = "province")
    private City              city;

    @DodoViewGroup(groupSeq = 1, groupName = "第一组级联")
    @DodoField(name = "区域", sortSeq = 5)
    @DodoCascade(group = 0, parentField = "city")
    private District          area;

    @DodoViewGroup(groupSeq = 2, groupName = "第二组级联")
    @DodoField(name = "国家2", sortSeq = 2, infoTip = "切换时，省份（2）省份（3） 都级联", isPopup = true)
    @DodoCascade(group = 1)
    private Country           country2;

    @DodoViewGroup(groupSeq = 2, groupName = "第二组级联")
    @DodoField(name = "省份（2）", sortSeq = 3, infoTip = "切换时，城市（2）级联")
    @DodoCascade(group = 1, parentField = "country2")
    private Province          province2;

    @DodoViewGroup(groupSeq = 2, groupName = "第二组级联")
    @DodoField(name = "省份（3）", sortSeq = 3, infoTip = "切换时，城市（3）级联")
    @DodoCascade(group = 1, parentField = "country2")
    private Province          province3;

    @DodoViewGroup(groupSeq = 2, groupName = "第二组级联")
    @DodoField(name = "城市（2）", sortSeq = 4, infoTip = "切换时，区域（2）级联")
    @DodoCascade(group = 1, parentField = "province2")
    private City              city2;

    @DodoViewGroup(groupSeq = 2, groupName = "第二组级联")
    @DodoField(name = "城市（3）", sortSeq = 4, infoTip = "切换时，无级联")
    @DodoCascade(group = 1, parentField = "province3")
    private City              city3;

    @DodoViewGroup(groupSeq = 2, groupName = "第二组级联")
    @DodoField(name = "区域（2）", sortSeq = 5)
    @DodoCascade(group = 1, parentField = "city2")
    private District          area2;

    @OneToOne
    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    @OneToOne
    public Province getProvince() {
        return province;
    }

    public void setProvince(Province province) {
        this.province = province;
    }

    @OneToOne
    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    @OneToOne
    public District getArea() {
        return area;
    }

    public void setArea(District area) {
        this.area = area;
    }

    @OneToOne
    public Country getCountry2() {
        return country2;
    }

    public void setCountry2(Country country2) {
        this.country2 = country2;
    }

    @OneToOne
    public Province getProvince2() {
        return province2;
    }

    public void setProvince2(Province province2) {
        this.province2 = province2;
    }

    @OneToOne
    public Province getProvince3() {
        return province3;
    }

    public void setProvince3(Province province3) {
        this.province3 = province3;
    }

    @OneToOne
    public City getCity2() {
        return city2;
    }

    public void setCity2(City city2) {
        this.city2 = city2;
    }

    @OneToOne
    public City getCity3() {
        return city3;
    }

    public void setCity3(City city3) {
        this.city3 = city3;
    }

    @OneToOne
    public District getArea2() {
        return area2;
    }

    public void setArea2(District area2) {
        this.area2 = area2;
    }

}
