package com.ihorak.truffle.node.special_form.quasiquote;

import com.ihorak.truffle.runtime.SchemeList;

public record UnquoteSplicingInsertInfo(
        SchemeList previousCell,
        SchemeList cellToReplace) { }
