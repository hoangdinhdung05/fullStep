package vn.fullStep.controller.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class PageResponseAbstract implements java.io.Serializable {
    public int pageNumber;
    public int pageSize;
    public long totalElements;
    public long totalPages;
}
