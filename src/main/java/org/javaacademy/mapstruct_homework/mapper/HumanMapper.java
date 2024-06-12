package org.javaacademy.mapstruct_homework.mapper;

import org.javaacademy.mapstruct_homework.dto.PersonCreditDto;
import org.javaacademy.mapstruct_homework.dto.PersonDriverLicenceDto;
import org.javaacademy.mapstruct_homework.dto.PersonInsuranceDto;
import org.javaacademy.mapstruct_homework.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

@Mapper
public interface HumanMapper {
    @Mapping(target = "passportNumber", source = ".", qualifiedByName = "getPassportData")
    @Mapping(target = "salary", source = ".", qualifiedByName = "getSalaryWithCurrency")
    @Mapping(target = "fullAddress", source = ".", qualifiedByName = "getFullAddress")
    @Mapping(target = "id", ignore = true)
    PersonCreditDto convertToCreditDto(Human human);

    @Mapping(target = "fullName", source = ".", qualifiedByName = "getFullName")
    @Mapping(target = "fullPassportData", source = ".", qualifiedByName = "getFullPassportData")
    @Mapping(target = "birthDate", source = ".", qualifiedByName = "getBirthDate")
    PersonDriverLicenceDto convertToDriverLicenceDto(Human human);

    @Mapping(target = "fullName", source = ".", qualifiedByName = "getFullName")
    @Mapping(target = "fullAddress", source = ".", qualifiedByName = "getFullAddress")
    @Mapping(target = "fullAge", source = ".", qualifiedByName = "getFullAge")
    PersonInsuranceDto convertToInsuranceDto(Human human);

    @Named("getPassportData")
    default String getPassportData(Human human) {
        Passport passport = human.getPassport();
        return "%s %s".formatted(passport.getSeries(),
                passport.getNumber());
    }

    @Named("getSalaryWithCurrency")
    default String getSalaryWithCurrency(Human human) {
        Work work = human.getWork();
        return "%s %s".formatted(work.getSalary(),
                work.getCurrency());
    }

    @Named("getFullAddress")
    default String getFullAddress(Human human) {
        Address address = human.getLivingAddress();
        return checkNullString(address.getRegion(),
                address.getCity(),
                address.getStreet(),
                address.getHouse(),
                address.getFlat())
                .trim();
    }

    @Named("getFullName")
    default String getFullName(Human human) {
        return "%s %s %s".formatted(human.getFirstName(),
                        human.getLastName(),
                        human.getMiddleName())
                .trim();
    }

    @Named("getFullPassportData")
    default String getFullPassportData(Human human) {
        Passport passport = human.getPassport();
        return "%s%s %s".formatted(passport.getSeries(),
                passport.getNumber(),
                DateTimeFormatter.ofPattern("d.M.yyyy").format(passport.getIssueDate()));
    }

    @Named("getBirthDate")
    default String getBirthDate(Human human) {
        return "%s.%s.%s".formatted(human.getBirthDay(),
                human.getBirthMonth(),
                human.getBirthYear());
    }

    @Named("getFullAge")
    default Integer getFullAge(Human human) {
        return LocalDate.now().getYear() - human.getBirthYear();
    }

    private String checkNullString(String ...str) {
        return Arrays.stream(str).filter(Objects::nonNull).collect(Collectors.joining(" "));
    }
}