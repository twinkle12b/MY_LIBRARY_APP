import { Link } from "react-router-dom";
import BookModel from "../../models/BookModel";
import { LeaveAReview } from "../utils/LeaveAReview";

export const CheckoutAndReviewBox: React.FC<{
    book: BookModel | undefined, mobile: boolean, currentLoanCount: number, isAuthenticated: any,
    isCheckedOut: Boolean, checkoutBookByUser: any, isUserReviewLeft: boolean, submitAReview: any
}> = (props) => {

    function reviewRender() {
        if(props.isAuthenticated && !props.isUserReviewLeft) {
            return(<p>
                <LeaveAReview submitAReview={props.submitAReview} />
            </p>);

        } else if(props.isAuthenticated && props.isUserReviewLeft) {
            return(<p><b>Thank you for your Review!</b></p>);

        } else {
            return (<div><hr/><p>Sign in to be able to leave a review.</p></div>);
        }
    }



    function buttonRender() {
        if (props.isAuthenticated) {
            if (!props.isCheckedOut && props.currentLoanCount < 5) {
                return (<button onClick={() => props.checkoutBookByUser()} className="btn btn-success btn-lg">Checkout</button>)
            } else if (props.isCheckedOut) {
                return (<p><b>Book Checked Out. Enjoy!</b></p>)
            } else if (!props.isCheckedOut) {
                return (<p className="text-danger">Too many books Checked out !!</p>)
            }
        }
        return (<Link to='/login' className='btn btn-success btn-lg'>Sign in</Link>)

    }



    return (
        <div className={props.mobile ? 'card d-flex mt-5' : 'card col-3 container d-flex mb-5'}>
            <div className='card-body container'>
                <div className='mt-3'>
                    <p>
                        <b>{props.currentLoanCount}/5 </b>
                        books checked out
                    </p>
                    <hr />
                    {props.book && props.book.copiesAvailable && props.book.copiesAvailable > 0 ?
                        <h4 className='text-success'>
                            Available
                        </h4>
                        :
                        <h4 className='text-danger'>
                            Wait List
                        </h4>
                    }
                    <div className='row'>
                        <p className='col-6 lead'>
                            <b>{props.book?.copies} </b>
                            copies
                        </p>
                        <p className='col-6 lead'>
                            <b>{props.book?.copiesAvailable} </b>
                            available
                        </p>
                    </div>
                </div>
                {buttonRender()}
                <hr />
                <p className='mt-3'>
                    This number can change until placing order has been complete.
                </p>
               {reviewRender()}
            </div>
        </div>
    );
}