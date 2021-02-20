package lotto.controller;

import lotto.domain.LottoNumber;
import lotto.domain.LottoResult;
import lotto.domain.LottoTicket;
import lotto.domain.LottoTickets;
import lotto.domain.Money;
import lotto.domain.WinningLotto;
import lotto.domain.ticketFactory.TicketFactory;
import lotto.exception.LottoCustomException;
import lotto.view.InputView;
import lotto.view.OutputView;

public class LottoController {

    private final InputView inputView;
    private final TicketFactory ticketFactory;
    private final LottoResult lottoResult;

    private Money money;
    private LottoTickets lottoTickets;

    public LottoController() {
        inputView = new InputView();
        ticketFactory = new TicketFactory();
        lottoResult = new LottoResult();
    }

    public void run() {
        money = inputMoney();
        lottoTickets = buyTickets();
        WinningLotto winningLotto = inputWinningLotto();
        showResult(winningLotto);
    }

    private Money inputMoney() {
        try {
            OutputView.printMoneyMessage();
            Money money = new Money(inputView.inputValue());
            OutputView.printTicketCountMessage(money.countTickets());
            return money;
        } catch (LottoCustomException exception) {
            OutputView.printErrorMessage(exception);
            return inputMoney();
        }
    }

    private LottoTickets buyTickets() {
        lottoTickets = ticketFactory.makeRandomTicketsByCount(money.countTickets());
        OutputView.printAllTickets(lottoTickets);
        return lottoTickets;
    }

    private WinningLotto inputWinningLotto() {
        LottoTicket lottoTicket = inputWinningNumbers();
        LottoNumber bonus = inputBonus(lottoTicket);
        return new WinningLotto(lottoTicket, bonus);
    }

    private LottoTicket inputWinningNumbers() {
        try {
            OutputView.printWinningNumbers();
            return ticketFactory.makeFixedTicket(inputView.inputNumbers());
        } catch (LottoCustomException exception) {
            OutputView.printErrorMessage(exception);
            return inputWinningNumbers();
        }
    }

    private LottoNumber inputBonus(LottoTicket lottoTicket) {
        try {
            OutputView.printBonusNumber();
            LottoNumber bonusNumber = new LottoNumber(inputView.inputValue());
            validateDuplicate(lottoTicket, bonusNumber);
            return bonusNumber;
        } catch (LottoCustomException exception) {
            OutputView.printErrorMessage(exception);
            return inputBonus(lottoTicket);
        }
    }

    private void validateDuplicate(LottoTicket lottoTicket, LottoNumber bonusNumber) {
        if (lottoTicket.hasNumber(bonusNumber)) {
            throw new LottoCustomException("보너스 볼은 지난 주 당첨번호와 중복될 수 없습니다.");
        }
    }

    private void showResult(WinningLotto winningLotto) {
        lottoResult.checkWinnings(lottoTickets, winningLotto);

        OutputView.printWinningResultTitle();
        OutputView.printProfit(money.calculateProfit(lottoResult.calculateTotalReward()),
            lottoResult.getResults());
    }
}
