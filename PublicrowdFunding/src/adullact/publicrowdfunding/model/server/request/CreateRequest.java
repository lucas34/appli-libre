package adullact.publicrowdfunding.model.server.request;

import adullact.publicrowdfunding.model.local.ressource.Account;
import adullact.publicrowdfunding.model.local.ressource.Resource;
import adullact.publicrowdfunding.model.server.entities.SimpleServerResponse;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import adullact.publicrowdfunding.model.server.errorHandler.CreateErrorHandler;
import adullact.publicrowdfunding.model.server.event.CreateEvent;

public class CreateRequest<TResource extends Resource<TResource, TServerResource, TDetailedServerResource>, TServerResource, TDetailedServerResource extends TServerResource>
		extends
		AnonymousRequest<CreateRequest<TResource, ?, ?>, CreateEvent<TResource>, CreateErrorHandler<TResource>> {
	private TResource m_resource;

	public CreateRequest(TResource resource, CreateEvent<TResource> event) {
		super(event, new CreateErrorHandler<TResource>());

		this.m_resource = resource;
	}

	@Override
	public void execute() {
		System.out.println("exécution");
		m_resource.methodPOST(service()).subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.onErrorReturn(new Func1<Throwable, SimpleServerResponse>() {

					@Override
					public SimpleServerResponse call(Throwable throwable) {
						return null;
					}

				}).subscribe(new Action1<SimpleServerResponse>() {

					@Override
					public void call(SimpleServerResponse response) {
                        if (response == null) {
                            errorHandler().manageCallback();
                            return;
                        }

                        done();
                        m_resource.setResourceId(response.idAffected);
                        event().onCreate(m_resource);
                    }
				}, new Action1<Throwable>() {
					@Override
					public void call(Throwable throwable) {
						// on main thread; something went wrong
						System.out.println("Error! " + throwable);
					}
				}, new Action0() {
					@Override
					public void call() {
						System.out.println("Hello");
					}
				});
	}
}