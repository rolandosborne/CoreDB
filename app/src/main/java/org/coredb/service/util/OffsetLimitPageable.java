package org.coredb.service.util;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.*;

public class OffsetLimitPageable extends PageRequest {
    private int offset;
    public OffsetLimitPageable(int offset, int limit, Sort sort){
        super(offset, limit, sort);
        this.offset=offset;
    }
    @Override
    public int getOffset(){
        return this.offset;
    }
}

