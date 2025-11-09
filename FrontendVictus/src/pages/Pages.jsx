import { Route,Routes } from "react-router-dom"
import HomePage from "./HomePage"
import LoginAdminPage from "./LoginAdminPage"
import ShowAdmins from "../components/ShowAdmins"
import AdminManagement from "../components/AdminManagement"
import CommonZone from "../components/CommonZone"
import Property from "../components/Property"
import Loading from "../loaders/LoadingCircle"
// import Loading from "../loaders/LoadingText"


function Pages() {
  return (
    <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/loginAdmin" element={<LoginAdminPage />} />
        <Route path="/ShowAdmins" element={<ShowAdmins />} />
        <Route path="/ManagementAdmin" element={<AdminManagement />} />
        <Route path="/CommonZone" element={<CommonZone />} />
        <Route path="/Property" element={<Property />} />
        <Route path="/Loading" element={<Loading />} />
        <Route path="*" element={<HomePage />} />


    </Routes>
  )
}

export default Pages
