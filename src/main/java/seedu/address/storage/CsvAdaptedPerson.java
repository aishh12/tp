package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Github;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Telegram;
import seedu.address.model.tag.Tag;

/**
 * A Jackson-CSV-Friendly version of {@link Person}
 */
@JsonPropertyOrder({"name", "github", "telegram", "address", "phone", "email", "tagged"}) // sequence in csv
public class CsvAdaptedPerson {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Person's %s field is missing!";

    private final String name;
    private final String telegram;
    private final String github;
    private final String phone;
    private final String email;
    private final String address;
    private String tagged = "";

    /**
     * Constructs a {@code CsvAdaptedPerson} with the given person details.
     */
    public CsvAdaptedPerson(String name, String telegram, String github, String phone,
            String email, String address, String tagged) {
        this.name = name;
        this.telegram = telegram;
        this.github = github;
        this.phone = phone;
        this.email = email;
        this.address = address;
        if (tagged != null) {
            this.tagged = tagged;
        }
    }

    /**
     * Converts a given {@code Person} into this class for Jackson use.
     */
    public CsvAdaptedPerson(Person source) {
        name = source.getName().fullName;
        telegram = source.getTelegram().value;
        github = source.getGithub().value;
        phone = source.getPhone().value;
        email = source.getEmail().value;
        address = source.getAddress().value;
        List<String> tagsString = source.getTags()
                .stream()
                .map(tag -> tag.getStringType() + tag.tagName)
                .collect(Collectors.toList());
        tagged = String.join(" ", tagsString);
    }

    public String getName() {
        return name;
    }

    public String getTelegram() {
        return telegram;
    }

    public String getGithub() {
        return github;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getTagged() {
        return tagged;
    }

    /**
     * Converts this Jackson-friendly adapted person object into the model's {@code Person} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person.
     */
    public Person toModelType() throws IllegalValueException {
        if (tagged == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Tag.class.getSimpleName()));

        }
        List<Tag> personTags = new ArrayList<>();
        String[] stringTags = tagged.split(" ");
        for (String tagString : stringTags) {
            if (tagString.isEmpty()) {
                continue;
            } else if (!Tag.isValidTagName(tagString)) {
                throw new IllegalValueException(Tag.MESSAGE_CONSTRAINTS);
            }
            personTags.add(new Tag(tagString));
        }

        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }
        final Name modelName = new Name(name);

        if (telegram == null) {
            throw new IllegalValueException(
                    String.format(MISSING_FIELD_MESSAGE_FORMAT, Telegram.class.getSimpleName()));
        }
        if (!Telegram.isValidTelegram(telegram)) {
            throw new IllegalValueException(Telegram.MESSAGE_CONSTRAINTS);
        }
        final Telegram modelTelegram = new Telegram(telegram);

        if (github == null) {
            throw new IllegalValueException(
                    String.format(MISSING_FIELD_MESSAGE_FORMAT, Github.class.getSimpleName()));
        }
        if (!Github.isValidGithub(github)) {
            throw new IllegalValueException(Github.MESSAGE_CONSTRAINTS);
        }
        final Github modelGithub = new Github(github);

        if (phone == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName()));
        }

        if (!phone.isBlank() && !Phone.isValidPhone(phone)) {
            throw new IllegalValueException(Phone.MESSAGE_CONSTRAINTS);
        }
        final Phone modelPhone = new Phone(phone);

        if (email == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName()));
        }
        if (!email.isBlank() && !Email.isValidEmail(email)) {
            throw new IllegalValueException(Email.MESSAGE_CONSTRAINTS);
        }
        final Email modelEmail = new Email(email);

        if (address == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Address.class.getSimpleName()));
        }
        if (!address.isBlank() && !Address.isValidAddress(address)) {
            throw new IllegalValueException(Address.MESSAGE_CONSTRAINTS);
        }
        final Address modelAddress = new Address(address);

        final Set<Tag> modelTags = new HashSet<>(personTags);
        return new Person(modelName, modelTelegram, modelGithub, modelPhone, modelEmail, modelAddress, modelTags);
    }
}
