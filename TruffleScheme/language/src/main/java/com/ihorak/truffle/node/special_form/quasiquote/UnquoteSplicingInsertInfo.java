package com.ihorak.truffle.node.special_form.quasiquote;

import com.ihorak.truffle.type.SchemeCell;

public record UnquoteSplicingInsertInfo(
        SchemeCell previousCell,
        SchemeCell cellToReplace) { }