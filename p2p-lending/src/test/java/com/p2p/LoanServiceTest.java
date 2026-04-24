package com.p2p;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

import com.p2p.domain.Borrower;
import com.p2p.domain.Loan;
import com.p2p.service.LoanService;

public class LoanServiceTest {
    
    // TC-01
    @Test
    void shouldRejectLoanWhenBorrowerNotVerified() {

        // =====================================================
        // SCENARIO:
        // Borrower tidak terverifikasi (KYC = false)
        // Ketika borrower mengajukan pinjaman
        // Maka sistem harus menolak dengan melempar exception
        // =====================================================

        // =========================
        // Arrange (Initial Condition)
        // =========================
        // Borrower belum lolos proses KYC
        Borrower borrower = new Borrower(false, 700);

        // Service untuk pengajuan loan
        LoanService loanService = new LoanService();

        // Jumlah pinjaman valid
        BigDecimal amount = BigDecimal.valueOf(1000);

        // =========================
        // Assert (Expected Result)
        // =========================
        assertThrows(IllegalArgumentException.class, () -> {
            loanService.createLoan(borrower, amount);
        });
    }

    // TC-02
    @Test
    void shouldRejectLoanWhenAmountIsZeroOrNegative() {

        // =========================
        // Arrange
        // =========================
        // Borrower valid
        Borrower borrower = new Borrower(true, 700);

        LoanService loanService = new LoanService();

        // =========================
        // Act + Assert
        // =========================

        // amount = 0
        assertThrows(IllegalArgumentException.class, () -> {
            loanService.createLoan(borrower, BigDecimal.ZERO);
        });

        // amount negatif
        assertThrows(IllegalArgumentException.class, () -> {
            loanService.createLoan(borrower, BigDecimal.valueOf(-100));
        });
    }

    // TC-03
    @Test
    void shouldRejectLoanWhenCreditScoreTooLow() {

        Borrower borrower = new Borrower(true, 500); // credit score rendah
        LoanService loanService = new LoanService();

        Loan loan = loanService.createLoan(borrower, new BigDecimal("1000"));

        assertEquals(Loan.Status.REJECTED, loan.getStatus());
    }

    // TC-04
    @Test
    void shouldApproveLoanWhenCreditScoreIsSufficient() {

        Borrower borrower = new Borrower(true, 700); // score tinggi
        LoanService loanService = new LoanService();

        Loan loan = loanService.createLoan(borrower, new BigDecimal("1000"));

        assertEquals(Loan.Status.APPROVED, loan.getStatus());
    }
}
