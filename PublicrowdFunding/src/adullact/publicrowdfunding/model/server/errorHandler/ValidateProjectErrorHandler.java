package adullact.publicrowdfunding.model.server.errorHandler;

import retrofit.RetrofitError;
import adullact.publicrowdfunding.model.server.event.ValidateProjectEvent;
import adullact.publicrowdfunding.model.server.request.ValidateProjectRequest;

public class ValidateProjectErrorHandler extends AuthenticatedErrorHandler<ValidateProjectRequest, ValidateProjectEvent, ValidateProjectErrorHandler> {

}
