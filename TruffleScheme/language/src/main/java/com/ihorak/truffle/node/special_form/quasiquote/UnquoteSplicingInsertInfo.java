package com.ihorak.truffle.node.special_form.quasiquote;

import com.ihorak.truffle.type.SchemeList;

public record UnquoteSplicingInsertInfo(
        SchemeList previousCell,
        SchemeList cellToReplace) { }
