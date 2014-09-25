package com.femtioprocent.fpd2.util;

import java.util.Date;

public class DateRange {
    public Date from;
    public Date to;
    
    public DateRange left;
    Op op = Op.NONE;
    public DateRange right;
    
    public static enum Op {
        NONE, OR, AND, NOT
    }
    
    public DateRange(Date from, Date to) {
        this.from = from;
        this.to = to;
    }

    public DateRange(DateRange left, Op op, DateRange right) {
        this.left = left;
        this.op = op;
        this.right = right;
    }
    
    public boolean inRange(Date date) {
        if ( op == Op.AND ) {
            return left.inRange(date) && right.inRange(date);
        } else if ( op == Op.OR ) {
            return left.inRange(date) || right.inRange(date);
        } else if ( op == Op.NOT ) {
            return ! left.inRange(date);
        }
        return date.equals(from) || date.after(from) && date.before(to);
    }
    
    public String toString() {
        if ( op == Op.AND ) {
            return "(" + left + " AND " + right + ")";
        } else if ( op == Op.OR ) {
            return "(" + left + " OR " + right + ")";
        } else if ( op == Op.NOT ) {
            return "!" + left;
        }
        return "[" + from + '-' + to + "]";
    }
}
