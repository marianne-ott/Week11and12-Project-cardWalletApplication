package no.cardwallet.card.GiftCard;

import no.cardwallet.card.AppUser.AppUserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@Controller
public class GiftCardController {

    final
    GiftCardRepository giftCardRepository;

    final
    AppUserRepository appUserRepository;

    public GiftCardController(GiftCardRepository giftCardRepository, AppUserRepository appUserRepository) {
        this.giftCardRepository = giftCardRepository;
        this.appUserRepository = appUserRepository;
    }

    //part of refactoring
    private Long getAppUserId(Principal principal) {
        String email = principal.getName();
        return appUserRepository.findByEmail(email).getId();
    }

    //part of refactoring
    private List<GiftCard> getAllCards(Principal principal) {
        Long appUserId = getAppUserId(principal);
        return giftCardRepository.findGiftCardsByAppUserId(appUserId);
    }

    //part of refactoring
    private boolean checkPrincipalIsCardOwner(@PathVariable Long appUserId, @PathVariable Long cardId, Principal principal) {
        Long principalUserId = getAppUserId(principal);
        List<GiftCard> giftCardList = giftCardRepository.findGiftCardsByAppUserId(principalUserId);
        return !giftCardList.contains(giftCardRepository.findGiftCardById(cardId)) || !principalUserId.equals(appUserId);
    }

    //  Main page - Show all gift cards of user, by id
    @GetMapping("/my-cards")
    public String getAllCards(Model model, Principal principal) {
        List<GiftCard> giftCardList = getAllCards(principal);
        boolean isExpired = false;
        for (GiftCard giftCard : giftCardList) {
            LocalDate expDate = (giftCard.getExpiryDate().toLocalDate());
            if (LocalDate.now().isAfter(expDate)) {
                isExpired = true;
            }
            giftCard.setExpired(isExpired);
            isExpired = false;
        }
        model.addAttribute("giftCardList", giftCardList);
        return "myCards";
    }

    //  Show gift card details
    @GetMapping("/show-gift-card/{appUserId}/{cardId}")
    public String showGiftCard(Model model, @PathVariable Long appUserId, @PathVariable Long cardId, Principal principal) {
        if (checkPrincipalIsCardOwner(appUserId, cardId, principal)) return "defaultView";
        GiftCard giftCard = giftCardRepository.findGiftCardById(cardId);

        boolean isExpired = false;
        LocalDate expDate = (giftCard.getExpiryDate().toLocalDate());
        if (LocalDate.now().isAfter(expDate)) {
            isExpired = true;
        }

        giftCard.setExpired(isExpired);
        model.addAttribute("giftCard", giftCard);
        model.addAttribute("appUserId", appUserId);

        return "showGiftCard";
    }

    //  Add gift card
    @GetMapping("/add-gift-card")
    public String addGiftCard(Model model, Principal principal) {
        Long appUserId = getAppUserId(principal);
        model.addAttribute("appUserId", appUserId);
        model.addAttribute("giftCard", new GiftCard());

        return "addGiftCard";
    }

    //  Save new gift card
    @PostMapping("/save-new-gift-card")
    public String saveNewGiftCard(@ModelAttribute GiftCard giftCard, Principal principal) {
        Long appUserId = getAppUserId(principal);
        giftCard.setAppUserId(appUserId);
        giftCardRepository.save(giftCard);

        return "redirect:/my-cards";
    }

    @GetMapping("/edit-gift-card/{appUserId}/{cardId}")
    public String editGiftCard(Model model, @PathVariable Long appUserId, @PathVariable Long cardId, Principal principal) {
        GiftCard tempGiftCard = new GiftCard();
        model.addAttribute("tempGiftCard", tempGiftCard);

        if (checkPrincipalIsCardOwner(appUserId, cardId, principal)) return "defaultView";
        GiftCard giftCard = giftCardRepository.findById(cardId).get();

        model.addAttribute(giftCard);

        return "editGiftCard";
    }

    @PostMapping("/save-edited-gift-card/{appUserId}/{cardId}")
    public String savEditedGiftCard(@ModelAttribute GiftCard giftCard, @ModelAttribute GiftCard tempGiftCard, @PathVariable Long appUserId, @PathVariable Long cardId, Principal principal) {
        if (checkPrincipalIsCardOwner(appUserId, cardId, principal)) return "defaultView";
        if (cardId != null) {
            giftCard.setId(cardId);
        }
        giftCard.setAppUserId(getAppUserId(principal));
        giftCard.setExpiryDate(giftCardRepository.findGiftCardById(cardId).getExpiryDate()); // Should we allow the user to change the expiry date?
        giftCardRepository.save(giftCard);
        giftCard.setBalanceInt(tempGiftCard.getBalanceInt());

        return "redirect:/my-cards";
    }

    @GetMapping("/delete-gift-card/{appUserId}/{cardId}")
    public String deleteGiftCard(@PathVariable Long appUserId, @PathVariable Long cardId) {
        giftCardRepository.deleteById(cardId);
        return "redirect:/my-cards";
    }

}