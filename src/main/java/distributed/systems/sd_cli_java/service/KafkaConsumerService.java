// package distributed.systems.sd_cli_java.service;

// import org.springframework.stereotype.Service;

// import lombok.RequiredArgsConstructor;
// import lombok.extern.slf4j.Slf4j;

// @Service
// @RequiredArgsConstructor
// @Slf4j
// public class KafkaConsumerService {

// private final PlanService planService;
// private final PlanRepository planRepository;
// private final UserService userService;
// private final ExpenseService expenseService;
// private final KafkaProducerService kafkaProducerService;

// @KafkaListener(topics = "join_plan", containerFactory =
// "kafkaListenerContainerFactory")
// @Transactional
// public void consumeExpenseNotification(ExpenseNotificationDTO notification) {
// try {
// log.info("Received join plan request for user: {} to plan: {}",
// notification.getUsername(), notification.getPlanId());

// // Validate required fields
// if (notification.getPlanId() == null || notification.getUsername() == null) {
// log.warn("Cannot process join plan request: missing username or planId");
// return;
// }

// // Call the plan service to add user to plan
// boolean success = planService.addUserToPlanByUsername(
// notification.getUsername(),
// notification.getPlanId());

// if (success) {
// log.info("Successfully processed join plan request for user '{}' to plan {}",
// notification.getUsername(), notification.getPlanId());
// } else {
// log.warn("Failed to process join plan request (user might already be in plan
// or plan not found)");
// }
// } catch (Exception e) {
// log.error("Error processing join plan notification: {}", e.getMessage(), e);
// }
// }

// @KafkaListener(topics = "register_expense", containerFactory =
// "expenseRegistrationListenerContainerFactory")
// @Transactional
// public void consumeExpenseRegistration(ExpenseRegistrationDTO registration) {
// try {
// log.info("Received expense registration request from user: {} for plan: {}",
// registration.getUsername(), registration.getPlanId());

// if (registration.getExpense() != null) {
// log.info("Expense details: name={}, amount={}, type={}",
// registration.getExpense().getName(),
// registration.getExpense().getAmount(),
// registration.getExpense().getType());
// }

// // Validate required fields
// if (registration.getPlanId() == null || registration.getUsername() == null
// || registration.getExpense() == null || registration.getExpense().getAmount()
// == null) {
// log.warn("Cannot process expense registration: missing required fields");
// return;
// }

// // Find or create the user
// User user = userService.findOrCreateByUsername(registration.getUsername());

// // Find the plan with users explicitly fetched to avoid
// // LazyInitializationException
// Plan plan = planRepository.findByIdWithUsers(registration.getPlanId())
// .orElse(null);

// if (plan == null) {
// log.warn("Cannot process expense registration: plan with ID {} not found",
// registration.getPlanId());
// return;
// }

// // Check if user is part of the plan
// boolean userInPlan = false;
// for (User planUser : plan.getUsers()) {
// if (planUser.getUserId().equals(user.getUserId())) {
// userInPlan = true;
// break;
// }
// }

// // Add user to plan if not already a member
// if (!userInPlan) {
// log.info("User {} is not part of plan {}, adding user to plan",
// user.getUsername(), plan.getName());
// plan = planService.addUserToPlan(plan, user);
// }

// // Create expense entity using the data from the nested expense object
// Expense expense = Expense.builder()
// .name(registration.getExpense().getName() != null ?
// registration.getExpense().getName()
// : "Expense from " + user.getUsername())
// .amount(registration.getExpense().getAmount())
// .date(registration.getExpense().getDate() != null ?
// registration.getExpense().getDate()
// : LocalDateTime.now())
// .type(registration.getExpense().getType() != null ?
// registration.getExpense().getType() : "shared")
// .user(user)
// .plan(plan)
// .build();

// // Save expense
// Expense savedExpense = expenseService.createExpense(expense);

// log.info("Successfully registered expense: ID={}, amount={} for user='{}' in
// plan='{}'",
// savedExpense.getExpenseId(), savedExpense.getAmount(), user.getUsername(),
// plan.getName());

// // Send notification about the new expense
// kafkaProducerService.sendExpenseCreatedNotification(savedExpense);

// } catch (Exception e) {
// log.error("Error processing expense registration: {}", e.getMessage(), e);
// }
// }

// }