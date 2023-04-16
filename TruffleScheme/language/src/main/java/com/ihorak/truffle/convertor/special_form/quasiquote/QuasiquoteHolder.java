package com.ihorak.truffle.convertor.special_form.quasiquote;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.special_form.quasiquote.UnquoteSplicingInsertInfo;
import com.ihorak.truffle.runtime.SchemeList;

import java.util.List;

public record QuasiquoteHolder(List<SchemeExpression> unquoteToEval,
                        List<SchemeExpression> unquoteSplicingToEval,
                        List<SchemeList> unquoteToInsert,
                        List<UnquoteSplicingInsertInfo> unquoteSplicingToInsert) {
}
