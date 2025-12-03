# Backend Refactoring Plan

## Goal Description
Refactor backend code to use more concise and meaningful names for entities, classes, and variables.

## Proposed Changes

### Entities
- `MemberPackageAssigment` -> `Subscription`
- `MemberTrainerAssignment` -> `TrainerBooking`
- `MembershipPackage` -> `GymPlan`
- `TrainerProfile` -> `Trainer`
- `TrainerSchedule` -> `Schedule`

### Repositories
- `MemberPackageAssigmentRepository` -> `SubscriptionRepository`
- `MemberTrainerAssignmentRepository` -> `TrainerBookingRepository`
- `MembershipPackageRepository` -> `GymPlanRepository`
- `TrainerProfileRepository` -> `TrainerRepository`
- `TrainerScheduleRepository` -> `ScheduleRepository`

### Services
- `MemberPackageAssigmentService` -> `SubscriptionService`
- `MemberTrainerAssignmentService` -> `TrainerBookingService`
- `MembershipPackageService` -> `GymPlanService`
- `TrainerProfileService` -> `TrainerService`
- `TrainerScheduleService` -> `ScheduleService`

### Controllers
- `PackageController` -> `GymPlanController`
- `TrainerController` (if exists) or create/rename relevant controller.

### DTOs
- Rename Request/Response DTOs to match new Entity names (e.g., `MembershipPackageDTO` -> `GymPlanDTO`).

## Verification Plan
### Automated Tests
- Run `mvn clean compile` to ensure no compilation errors.
- Run `mvn spring-boot:run` to verify application startup.
