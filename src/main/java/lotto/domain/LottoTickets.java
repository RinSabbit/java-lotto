package lotto.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lotto.domain.ticketFactory.RandomTicketFactory;

public class LottoTickets {

    public static final int NUMBER_OF_WINNING_TYPE = 8;

    private final List<LottoTicket> lottoTickets;

    public LottoTickets() {
        this.lottoTickets = new ArrayList<>();
    }

    public LottoTickets(List<LottoTicket> lottoTickets) {
        this.lottoTickets = lottoTickets;
    }

    public void makeTicketByCount(int counts) {
        for (int i = 0; i < counts; i++) {
            lottoTickets.add(RandomTicketFactory.makeTicket());
        }
    }

    public List<Integer> checkHitCount(LottoTicket winningTicket, LottoNumber bonusBall) {
        List<Integer> winningCount = new ArrayList<>(
            Collections.nCopies(NUMBER_OF_WINNING_TYPE, 0));
        for (LottoTicket lottoTicket : lottoTickets) {
            int hitCount = lottoTicket.compareNumbers(winningTicket, bonusBall);
            winningCount.set(hitCount, winningCount.get(hitCount) + 1);
        }
        return winningCount;
    }

    public List<LottoTicket> toList() {
        return new ArrayList<>(lottoTickets);
    }
}
