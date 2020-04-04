import React, {Component} from "react";
import {connect} from "react-redux";
import {userAccount} from '../actions/AccountActions';
import DataService from '../service/DataService';
import { Link } from "react-router-dom";
import { accountEditError } from "../actions/ErrorActions";


class Account extends Component {

    password = 'sddsvwe34s';

    constructor(props) {
        super(props);
        this.handelSubmit = this.handelSubmit.bind(this);
    }

    async componentDidMount() {
        this.props.accountEditError(null);
        const response = await DataService.userProfileRequest();
        const account = await response.json();
        this.props.userAccount(account);
    }

    async handelSubmit(event) {
        event.preventDefault();
        const editForm = document.forms["edit-form"];
        const firstName = editForm.elements["firstName"].value;
        const lastName = editForm.elements['lastName'].value;
        const patronymic = editForm.elements['patronymic'].value;
        const email = editForm.elements['email'].value;
        const address = editForm.elements['address'].value;
        const phone = editForm.elements['phone'].value;
        const oldPassword = editForm.elements['oldPassword'].value;
        const newPassword = editForm.elements['newPassword'].value;
        const newAccountData = {
            firstName,
            lastName,
            patronymic,
            email,
            address,
            phone,
            oldPassword,
            newPassword
        };
        const response = await DataService.editAccountDataRequest(newAccountData);
        if (response.status === 403 || response.status === 406) {
            this.props.accountEditError(await response.json());
            return false;
        }
        this.props.accountEditError(null);
        const account = await response.json();
        this.props.userAccount(account);
        this.props.history.push('/catalog');
    }

    render() {
        const account = this.props.account || {};
        let error = this.props.error;
        const classesForInput = "form-control form-control-lg";
        const classesForInputWithAlert = "form-control form-control-lg is-invalid";
        const errorsFields = new Map();
        if (error) {
            if (error.errors)
                error.errors.forEach(item => errorsFields.set(item.field, item.message));
            else
                errorsFields.set(error.field, error.message);
        }
        return (
            <div className="w-25 account-edit-container">
                <h1 className="m-4">Account</h1>
                <form name="edit-form" onSubmit={ this.handelSubmit }>
                    <div className="form-group">
                        <label><b>Fist name</b></label>
                        <input type="text" className={errorsFields.get('fistName') ?
                            classesForInputWithAlert : classesForInput}
                               name="firstName" placeholder="Your fist name"
                               defaultValue={ account.firstName || '' } autoFocus/>
                        <div className="invalid-feedback">
                            { errorsFields.get('fistName') }
                        </div>
                    </div>
                    <div className="form-group">
                        <label><b>Last name</b></label>
                        <input type="text" className={errorsFields.get('lastName') ?
                            classesForInputWithAlert : classesForInput}
                               name="lastName" placeholder="Your last name"
                               defaultValue={ account.lastName || '' }/>
                        <div className="invalid-feedback">
                            { errorsFields.get('lastName') }
                        </div>
                    </div>
                    <div className="form-group">
                        <label><b>Patronymic</b></label>
                        <input type="text" className={errorsFields.get('patronymic') ?
                            classesForInputWithAlert : classesForInput}
                               name="patronymic" placeholder="Your patronymic"
                               defaultValue={ account.patronymic || '' }/>
                        <div className="invalid-feedback">
                            { errorsFields.get('patronymic') }
                        </div>
                    </div>
                    <div className="form-group">
                        <label><b>Email</b></label>
                        <input type="email" className={errorsFields.get('email') ?
                            classesForInputWithAlert : classesForInput}
                               name="email" placeholder="Your email"
                               defaultValue={ account.email  || '' }/>
                        <div className="invalid-feedback">
                            { errorsFields.get('email') }
                        </div>
                    </div>
                    <div className="form-group">
                        <label><b>Address</b></label>
                        <input type="text" className={errorsFields.get('address') ?
                            classesForInputWithAlert : classesForInput}
                               name="address" placeholder="Your address"
                               defaultValue={ account.address || '' }/>
                        <div className="invalid-feedback">
                            { errorsFields.get('address') }
                        </div>
                    </div>
                    <div className="form-group">
                        <label><b>Phone</b></label>
                        <input type="text" className={errorsFields.get('phone') ?
                            classesForInputWithAlert : classesForInput}
                               name="phone" placeholder="Your phone"
                               defaultValue={ account.phone || '' }/>
                            <div className="invalid-feedback">
                                { errorsFields.get('phone') }
                            </div>
                    </div>
                    <div className="form-group">
                        <label><b>Old password</b></label>
                        <input type="password" className={errorsFields.get('oldPassword') ?
                            classesForInputWithAlert : classesForInput}
                               name="oldPassword" placeholder="Old password"
                               defaultValue={this.password}/>
                        <div className="invalid-feedback">
                            { errorsFields.get('oldPassword') }
                        </div>
                    </div>
                    <div className="form-group">
                        <label><b>New password</b></label>
                        <input type="password" className={errorsFields.get('newPassword') ?
                            classesForInputWithAlert : classesForInput}
                               name="newPassword" placeholder="New password"
                               defaultValue={this.password}/>
                        <div className="invalid-feedback">
                            { errorsFields.get('newPassword') }
                        </div>
                    </div>
                    <div className="form-group mt-4 row">
                        <div className="col-6">
                            <button type="submit"
                                    className="btn btn-lg btn-primary btn-block">Save</button>
                        </div>
                        <div className="col-6">
                            <Link className="w-100 btn btn-lg btn-primary btn-warning" to="/catalog" role="button">Cancel</Link>
                        </div>
                    </div>
                </form>
            </div>
        );
    }
}

const mapStateToProps = (state) => {
    return {
        account: state.userState.user,
        error: state.errorState.error
    }
};

const mapDispatchToProps = (dispatch) => {
    return {
        userAccount: (account) => dispatch(userAccount(account)),
        accountEditError: (error) => dispatch(accountEditError(error))
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(Account);