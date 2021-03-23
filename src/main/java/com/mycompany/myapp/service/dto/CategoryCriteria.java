package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.UUIDFilter;
import io.github.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Category} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.CategoryResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /categories?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CategoryCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private UUIDFilter uuid;

    private StringFilter name;

    private StringFilter mcc;

    private UUIDFilter parentCategoryId;

    private BooleanFilter enabled;

    private StringFilter iconUrl;

    private IntegerFilter defaultOrderId;

    private ZonedDateTimeFilter addedDate;

    private ZonedDateTimeFilter updatedDate;

    private StringFilter regions;

    private StringFilter tags;

    public CategoryCriteria() {
    }

    public CategoryCriteria(CategoryCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.uuid = other.uuid == null ? null : other.uuid.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.mcc = other.mcc == null ? null : other.mcc.copy();
        this.parentCategoryId = other.parentCategoryId == null ? null : other.parentCategoryId.copy();
        this.enabled = other.enabled == null ? null : other.enabled.copy();
        this.iconUrl = other.iconUrl == null ? null : other.iconUrl.copy();
        this.defaultOrderId = other.defaultOrderId == null ? null : other.defaultOrderId.copy();
        this.addedDate = other.addedDate == null ? null : other.addedDate.copy();
        this.updatedDate = other.updatedDate == null ? null : other.updatedDate.copy();
        this.regions = other.regions == null ? null : other.regions.copy();
        this.tags = other.tags == null ? null : other.tags.copy();
    }

    @Override
    public CategoryCriteria copy() {
        return new CategoryCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public UUIDFilter getUuid() {
        return uuid;
    }

    public void setUuid(UUIDFilter uuid) {
        this.uuid = uuid;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getMcc() {
        return mcc;
    }

    public void setMcc(StringFilter mcc) {
        this.mcc = mcc;
    }

    public UUIDFilter getParentCategoryId() {
        return parentCategoryId;
    }

    public void setParentCategoryId(UUIDFilter parentCategoryId) {
        this.parentCategoryId = parentCategoryId;
    }

    public BooleanFilter getEnabled() {
        return enabled;
    }

    public void setEnabled(BooleanFilter enabled) {
        this.enabled = enabled;
    }

    public StringFilter getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(StringFilter iconUrl) {
        this.iconUrl = iconUrl;
    }

    public IntegerFilter getDefaultOrderId() {
        return defaultOrderId;
    }

    public void setDefaultOrderId(IntegerFilter defaultOrderId) {
        this.defaultOrderId = defaultOrderId;
    }

    public ZonedDateTimeFilter getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(ZonedDateTimeFilter addedDate) {
        this.addedDate = addedDate;
    }

    public ZonedDateTimeFilter getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(ZonedDateTimeFilter updatedDate) {
        this.updatedDate = updatedDate;
    }

    public StringFilter getRegions() {
        return regions;
    }

    public void setRegions(StringFilter regions) {
        this.regions = regions;
    }

    public StringFilter getTags() {
        return tags;
    }

    public void setTags(StringFilter tags) {
        this.tags = tags;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CategoryCriteria that = (CategoryCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(uuid, that.uuid) &&
            Objects.equals(name, that.name) &&
            Objects.equals(mcc, that.mcc) &&
            Objects.equals(parentCategoryId, that.parentCategoryId) &&
            Objects.equals(enabled, that.enabled) &&
            Objects.equals(iconUrl, that.iconUrl) &&
            Objects.equals(defaultOrderId, that.defaultOrderId) &&
            Objects.equals(addedDate, that.addedDate) &&
            Objects.equals(updatedDate, that.updatedDate) &&
            Objects.equals(regions, that.regions) &&
            Objects.equals(tags, that.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        uuid,
        name,
        mcc,
        parentCategoryId,
        enabled,
        iconUrl,
        defaultOrderId,
        addedDate,
        updatedDate,
        regions,
        tags
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CategoryCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (uuid != null ? "uuid=" + uuid + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (mcc != null ? "mcc=" + mcc + ", " : "") +
                (parentCategoryId != null ? "parentCategoryId=" + parentCategoryId + ", " : "") +
                (enabled != null ? "enabled=" + enabled + ", " : "") +
                (iconUrl != null ? "iconUrl=" + iconUrl + ", " : "") +
                (defaultOrderId != null ? "defaultOrderId=" + defaultOrderId + ", " : "") +
                (addedDate != null ? "addedDate=" + addedDate + ", " : "") +
                (updatedDate != null ? "updatedDate=" + updatedDate + ", " : "") +
                (regions != null ? "regions=" + regions + ", " : "") +
                (tags != null ? "tags=" + tags + ", " : "") +
            "}";
    }

}
