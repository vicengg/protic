import React from 'react';
import { useHistory } from 'react-router';
import { useGetWorkExperiences } from '../../hooks/useGetWorkExperiences';
import { CardSkeleton } from '../CardSkeleton';
import { OwnWorkExperience } from '../OwnWorkExperience';

export const MyWorkExperiencesView = () => {

    const [{ loading, data }, reload] = useGetWorkExperiences({ scope: 'own' });
    const skeletonPieces = [...Array(5).keys()];

    const history = useHistory();

    const goToAddExperiences = () => {
        history.push("/add-work-experience");
    };

    return (
        <>
            <div className="container">
                <h1>Mis experiencias laborales</h1>
                {!!loading && skeletonPieces.map(piece =>
                    <div key={piece} className="row mb-2">
                        <div className="col-md-12">
                            <CardSkeleton lines={8} />
                        </div>
                    </div>
                )}
                {!loading && !!data && data.result.map(workExperience =>
                    <div className="row mb-2" key={workExperience.id}>
                        <div className="col-md-12">
                            <OwnWorkExperience workExperience={workExperience} afterDelete={reload} editable={true} />
                        </div>
                    </div>
                )}
                {!loading && (!data || data.result.length === 0) &&
                    <div className="jumbotron">
                        <h1 className="display-4">¡Aún no has añadido una experiencia laboral!</h1>
                        <p className="lead">Para poder obtener información laboral de otros usuarios necesitas añadir tus propias experiencias laborales.</p>
                        <hr className="my-4" />
                        <p>Puedes añadir tus experiencias laborales desde el panel "Añadir experiencia".</p>
                        <p className="lead">
                            <button onClick={goToAddExperiences} className="btn btn-primary btn-lg">Añadir experiencia</button>
                        </p>
                    </div>
                }
            </div>
        </>
    );

};
