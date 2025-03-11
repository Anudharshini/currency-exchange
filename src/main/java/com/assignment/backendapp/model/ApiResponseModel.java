package com.assignment.backendapp.model;

import java.io.Serializable;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

/** The standard response model for all the API calls */
@Data
@NoArgsConstructor
public class ApiResponseModel<T extends Serializable> {

  /** Details of the error in API call. It would be <code>null</code> if there are no errors. */
  private ErrorResponseModel error;

  /** The data to be sent back as part of the API response */
  private List<T> data;

  /**
   * Pagination related data for API response It will be <code>null</code> if the response is not
   * paginated
   */
  private ApiResponseModel(List<T> data) {
    this.data = data;
  }

  /**
   * Builder api response model builder.
   *
   * @param <T> the type parameter
   * @return the api response model builder
   */
  public static <T extends Serializable> ApiResponseModelBuilder<T> builder() {
    return new ApiResponseModelBuilder<>();
  }

  /**
   * Builder class to build an instance of {@link ApiResponseModel}
   *
   * @param <T> the type parameter
   */
  public static class ApiResponseModelBuilder<T extends Serializable> {
    private ErrorResponseModel error;
    private List<T> data;

    public ApiResponseModelBuilder<T> error(ErrorResponseModel error) {
      this.error = error;
      return this;
    }

    /**
     * Data api response model builder.
     *
     * @param data the data
     * @return the api response model builder
     */
    public ApiResponseModelBuilder<T> data(List<T> data) {
      this.data = data;
      return this;
    }

    /**
     * Returns a new instance of {@link ApiResponseModel}, with the state set in the
     * multiple/chained calls
     *
     * @return the api response model
     */
    public ApiResponseModel<T> build() {
      ApiResponseModel<T> apiResponseModel = new ApiResponseModel<>();
      apiResponseModel.error = this.error;
      apiResponseModel.data = this.data;
      return apiResponseModel;
    }
  }
}
