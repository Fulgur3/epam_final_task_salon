package com.command;
import com.command.admin.*;
import com.command.common.ChangeLocaleCommand;
import com.command.common.ViewActivitiesCommand;
import com.command.common.ViewActivityCommand;
import com.command.user.*;
import com.command.common.RegisterCommand;
import com.command.user.ViewUserOrdersCommand;

public enum CommandType {
    REGISTER("register", new RegisterCommand(), CommandAccessLevel.GUEST),
    LOGIN("login", new LoginCommand(), CommandAccessLevel.GUEST),
    RESET_PASSWORD("resetPassword", new ResetPasswordCommand(), CommandAccessLevel.GUEST),
    LOGOUT("logout", new LogoutCommand(), CommandAccessLevel.USER),
    CHANGE_LOCALE("locale", new ChangeLocaleCommand(), CommandAccessLevel.GUEST),
    VIEW_USERS("viewUsers", new ViewUsersCommand(), CommandAccessLevel.ADMIN),
    CHANGE_USER_STATUS("changeUserStatus", new ChangeUserStatusCommand(), CommandAccessLevel.ADMIN),
    UPDATE_USER("updateUser", new UpdateUserCommand(), CommandAccessLevel.USER),
    ADD_MONEY_TO_CARD("addMoneyToCard", new AddMoneyToCardCommand(), CommandAccessLevel.USER),
    VIEW_USER_INFO("viewUserInfo", new ViewUserInfoCommand(), CommandAccessLevel.USER),
    ADD_ACTIVITY("addActivity", new AddActivityCommand(), CommandAccessLevel.ADMIN),
    EDIT_ACTIVITY("editActivity", new EditActivityCommand(), CommandAccessLevel.ADMIN),
    UPDATE_ACTIVITY("updateActivity", new UpdateActivityCommand(), CommandAccessLevel.ADMIN),
    VIEW_ACTIVITY("viewActivity", new ViewActivityCommand(), CommandAccessLevel.GUEST),
    VIEW_ACTIVITIES("viewActivities", new ViewActivitiesCommand(), CommandAccessLevel.GUEST),
    CREATE_ORDER("createOrder", new CreateOrderCommand(), CommandAccessLevel.USER),
    VIEW_ORDER("viewOrder", new ViewOrderCommand(), CommandAccessLevel.USER),
    ADD_ORDER("addOrder", new AddOrderCommand(), CommandAccessLevel.USER),
    PAY_FOR_ORDER("payForOrder", new PayForOrderCommand(), CommandAccessLevel.USER),
    CHANGE_ORDER_STATUS("changeOrderStatus", new ChangeOrderStatusCommand(), CommandAccessLevel.ADMIN),
    CANCEL_ORDER("cancelOrder", new CancelOrderCommand(), CommandAccessLevel.USER),
    VIEW_USER_ORDERS("viewUserOrders", new ViewUserOrdersCommand(), CommandAccessLevel.USER),
    VIEW_ALL_ORDERS("viewAllOrders", new ViewOrdersCommand(), CommandAccessLevel.ADMIN),
    ADD_REVIEW("addReview", new AddReviewCommand(), CommandAccessLevel.USER),
    EDIT_REVIEW("editReview", new EditReviewCommand(), CommandAccessLevel.USER),
    UPDATE_REVIEW("updateReview", new UpdateReviewCommand(), CommandAccessLevel.USER),
    DELETE_REVIEW("deleteReview", new DeleteReviewCommand(), CommandAccessLevel.ADMIN);

    private Command command;
    private String commandName;
    private CommandAccessLevel commandAccessLevel;

    CommandType(String commandName, Command command, CommandAccessLevel commandAccessLevel) {
        this.command = command;
        this.commandName = commandName;
        this.commandAccessLevel = commandAccessLevel;
    }

    public Command getCommand() {
        return command;
    }

    public String getName() {
        return commandName;
    }

    public CommandAccessLevel getCommandAccessLevel() {
        return commandAccessLevel;
    }
}