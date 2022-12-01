package softuni.exam.models.dto;


import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@NoArgsConstructor
@Getter
public class ImportAgentDTO {

        @Size(min = 2)
        @NotNull
        private String firstName;

        @Size(min = 2)
        @NotNull
        private String lastName;

        @Email
        @NotNull
        private String email;

        @NotNull
        @SerializedName("town")
        private String town;
}
