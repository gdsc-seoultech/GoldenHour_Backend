package com.gdsc.goldenhour.user.service;

import com.gdsc.goldenhour.common.exception.CustomCommonException;
import com.gdsc.goldenhour.common.exception.ErrorCode;
import com.gdsc.goldenhour.user.domain.ReliefGoods;
import com.gdsc.goldenhour.user.domain.User;
import com.gdsc.goldenhour.user.dto.request.ReliefGoodsReq;
import com.gdsc.goldenhour.user.dto.response.ReliefGoodsRes.ReliefGoodsReadRes;
import com.gdsc.goldenhour.user.repository.ReliefGoodsRepository;
import com.gdsc.goldenhour.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static com.gdsc.goldenhour.user.dto.response.ReliefGoodsRes.ReliefGoodsCreateRes;
import static com.gdsc.goldenhour.user.dto.response.ReliefGoodsRes.ReliefGoodsUpdateRes;

@RequiredArgsConstructor
@Service
public class ReliefGoodsService {

    private final UserService userService;
    private final ReliefGoodsRepository reliefGoodsRepository;

    @Transactional(readOnly = true)
    public List<ReliefGoodsReadRes> readReliefGoodsList(String userId) {
        User user = userService.readUser(userId);

        List<ReliefGoodsReadRes> response = new ArrayList<>();
        user.getReliefGoodsList().forEach(
                reliefGoods -> response.add(new ReliefGoodsReadRes(reliefGoods))
        );

        return response;
    }


    @Transactional
    public ReliefGoodsCreateRes createReliefGoods(ReliefGoodsReq request, String userId) {
        User user = userService.readUser(userId);

        ReliefGoods reliefGoods = request.toReliefGoods();
        user.addReliefGoods(reliefGoods);

        reliefGoodsRepository.save(reliefGoods);

        return new ReliefGoodsCreateRes(reliefGoods);
    }

    @Transactional
    public ReliefGoodsUpdateRes updateReliefGoods(ReliefGoodsReq request, Long reliefGoodsId, String userId) {
        User user = userService.readUser(userId);
        ReliefGoods reliefGoods = readReliefGoods(reliefGoodsId);

        validateUser(user, reliefGoods);

        reliefGoods.update(request);

        return new ReliefGoodsUpdateRes(reliefGoods);
    }

    @Transactional
    public void deleteReliefGoods(Long reliefGoodsId, String userId) {
        User user = userService.readUser(userId);
        ReliefGoods reliefGoods = readReliefGoods(reliefGoodsId);

        validateUser(user, reliefGoods);

        user.removeReliefGoods(reliefGoods);

        reliefGoodsRepository.deleteById(reliefGoodsId);
    }

    private ReliefGoods readReliefGoods(Long reliefGoodsId) {
        return reliefGoodsRepository.findById(reliefGoodsId)
                .orElseThrow(() -> new CustomCommonException(ErrorCode.ITEM_NOT_FOUND));
    }

    private void validateUser(User user, ReliefGoods reliefGoods) {
        if (!reliefGoods.getUser().equals(user)) {
            throw new CustomCommonException(ErrorCode.INVALID_USER);
        }
    }

}
