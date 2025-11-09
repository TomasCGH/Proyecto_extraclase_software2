import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import App from './App.jsx'
import { BrowserRouter } from 'react-router-dom'
import { AdminProviderWrapper } from './contexts/Admin.context.jsx'
import { PorteroProviderWrapper } from './contexts/Porteto.context.jsx'
import { CommonZoneProviderWrapper } from './contexts/CommonZone.context.jsx'
import { PropertyProviderWrapper } from './contexts/Property.context.jsx'

createRoot(document.getElementById('root')).render(
  //<StrictMode>
  <PorteroProviderWrapper>
    <AdminProviderWrapper>
      <CommonZoneProviderWrapper>
        <PropertyProviderWrapper>
          <BrowserRouter>
            <App />
          </BrowserRouter>
        </PropertyProviderWrapper>
      </CommonZoneProviderWrapper>
    </AdminProviderWrapper>
  </PorteroProviderWrapper>
  //</StrictMode>,
)
