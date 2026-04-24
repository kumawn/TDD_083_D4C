package com.p2p.service;
import java.math.BigDecimal;

import com.p2p.domain.Borrower;
import com.p2p.domain.Loan;

public class LoanService {
    public Loan createLoan(Borrower borrower, BigDecimal amount) {

        validateBorrower(borrower);
        validateAmount(amount);

        Loan loan = new Loan();

        if (isEligible(borrower)) {
            loan.approve();
        } else {
            loan.reject();
        }

        return loan;
    }

    private void validateBorrower(Borrower borrower) {
        if (!borrower.isVerified()) {
            throw new IllegalArgumentException("Borrower not verified");
        }
    }

    private void validateAmount(BigDecimal amount){
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
    }

    private boolean isEligible(Borrower borrower) {
        return borrower.getCreditScore() >= 600;
    }
}
