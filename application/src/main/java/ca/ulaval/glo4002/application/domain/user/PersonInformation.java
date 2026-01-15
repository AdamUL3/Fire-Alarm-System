package ca.ulaval.glo4002.application.domain.user;

public record PersonInformation(
    PersonName name, PhoneNumber phoneNumber, PersonID id, ResponsibleStatus status) {}
