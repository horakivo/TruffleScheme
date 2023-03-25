package com.ihorak.truffle.convertor.SpecialForms.quasiquote;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.special_form.quasiquote.UnquoteSplicingInsertInfo;
import com.ihorak.truffle.type.SchemeCell;
import com.ihorak.truffle.type.SchemeList;

import java.util.List;

public record QuasiquoteHolder(List<SchemeExpression> unquoteToEval,
                        List<SchemeExpression> unquoteSplicingToEval,
                        List<SchemeList> unquoteToInsert,
                        List<UnquoteSplicingInsertInfo> unquoteSplicingToInsert) {
}
