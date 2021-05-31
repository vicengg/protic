import React from 'react';

export const Modal = ({ title, text, children, isShown, close }) => {

    return (
        <>
            <div className={`modal fade ${isShown ? "show d-block" : ""}`} role="dialog">
                <div className="modal-dialog" role="document">
                    <div className="modal-content">
                        <div className="modal-header">
                            <h5 className="modal-title">{title}</h5>
                            <button type="button" className="close" data-dismiss="modal" aria-label="Close" onClick={close}>
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <div className="modal-body">
                            <p>{text}</p>
                        </div>
                        {!!children && <div className="modal-footer">
                            {children}
                        </div>
                        }
                    </div>
                </div>
            </div>
        </>
    );
}