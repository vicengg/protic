import React from 'react';

export const Modal = ({ title, children, isShown, close, additionalClasses="" }) => {

    return (
        <>
            <div className={`modal fade ${isShown ? "show d-block" : ""}`} role="dialog">
                <div className={`modal-dialog ${additionalClasses}`} role="document">
                    <div className="modal-content">
                        <div className="modal-header">
                            <h5 className="modal-title">{title}</h5>
                            <button type="button" className="close" data-dismiss="modal" aria-label="Close" onClick={close}>
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <div className="modal-body">
                            {children[0]}
                        </div>
                        {!!children && <div className="modal-footer">
                            {children[1]}
                        </div>
                        }
                    </div>
                </div>
            </div>
        </>
    );
}